package com.pettoyou.server.domains.member.service.auth;

import com.pettoyou.server.config.jwt.util.TokenInfo;
import com.pettoyou.server.domains.auth.enums.TokenUserType;
import com.pettoyou.server.domains.auth.AuthTokenGenerator;
import com.pettoyou.server.domains.auth.OAuthInfoResponse;
import com.pettoyou.server.domains.auth.OAuthLoginParams;
import com.pettoyou.server.domains.auth.RequestOAuthInfoService;
import com.pettoyou.server.config.jwt.util.JwtUtil;
import com.pettoyou.server.domains.auth.enums.TokenType;
import com.pettoyou.server.config.redis.util.RedisUtil;
import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdmin;
import com.pettoyou.server.domains.hospital.repository.hospitalAdmin.HospitalAdminRepository;
import com.pettoyou.server.domains.hospital.repository.hospitalAdmin.HospitalAdminRoleRepository;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.entity.MemberRole;
import com.pettoyou.server.domains.member.entity.Role;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import com.pettoyou.server.domains.member.entity.enums.RoleType;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.member.repository.MemberRoleRepository;
import com.pettoyou.server.domains.member.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final HospitalAdminRepository hospitalAdminRepository;
    private final RoleRepository roleRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final AuthTokenGenerator authTokenGenerator;

    private static final String RT = "RT:";
    private static final String LOGOUT = "LOGOUT";

    @Override
    public AuthTokens signIn(OAuthLoginParams param) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(param);

        Member findMember = findMemberByOauthProviderAndProviderId(
                oAuthInfoResponse.getOAuthProvider(),
                oAuthInfoResponse.getId()
        ).orElseGet(() -> forceJoin(oAuthInfoResponse));

        List<RoleType> memberRoles = findMember.getAllMemberRole();

        String refreshToken = redisUtil.getData(RT + findMember.getEmail());
        if (refreshToken == null) {
            refreshToken = jwtUtil.createToken(findMember.getEmail(), memberRoles, TokenType.REFRESH_TOKEN, TokenUserType.MEMBER_TOKEN);
            redisUtil.setData(RT + findMember.getEmail(), refreshToken, jwtUtil.getExpiration(TokenType.REFRESH_TOKEN));
        }

        return authTokenGenerator.generateMemberTokenWithRFToken(findMember.getEmail(), memberRoles, refreshToken);
    }

    @Override
    public AuthTokens reissue(String resolveToken) {
        TokenInfo tokenInfo = jwtUtil.getInfoInTokenByTokenRoleType(resolveToken); // 이곳

        String refreshTokenInRedis = redisUtil.getData(RT + tokenInfo.infoInClaim()); // 이곳
        if (refreshTokenInRedis == null) {
            throw new CustomException(CustomResponseStatus.REFRESH_TOKEN_EXPIRED);
        }
        if (!Objects.equals(resolveToken, refreshTokenInRedis)) {
            throw new CustomException(CustomResponseStatus.REFRESH_TOKEN_NOT_MATCH);
        }

        if (tokenInfo.tokenUserType().equals(TokenUserType.MEMBER_TOKEN)) {
            Member findMember = memberRepository.findByEmail(tokenInfo.infoInClaim()).orElseThrow(
                    () -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND)
            );
            List<RoleType> memberRoles = findMember.getAllMemberRole();

            AuthTokens generateToken = authTokenGenerator.generateMemberTokenWithoutRFToken(tokenInfo.infoInClaim(), memberRoles);
            redisUtil.setData(RT + tokenInfo.infoInClaim(), generateToken.refreshToken(), jwtUtil.getExpiration(TokenType.REFRESH_TOKEN));

            return generateToken;
        }

        HospitalAdmin findHospitalAdmin = hospitalAdminRepository.findByUsername(tokenInfo.infoInClaim()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND)
        );
        List<RoleType> hospitalAdminRoles = findHospitalAdmin.getAllHospitalAdminRole();

        AuthTokens generateToken = authTokenGenerator.generateAdminTokenWithoutRFToken(tokenInfo.infoInClaim(), hospitalAdminRoles);
        redisUtil.setData(RT + tokenInfo.infoInClaim(), generateToken.refreshToken(), jwtUtil.getExpiration(TokenType.REFRESH_TOKEN));

        return generateToken;
    }

    @Override
    public void logout(String accessToken) {
        String resolveAccessToken = jwtUtil.resolveToken(accessToken);
        String emailInToken = jwtUtil.getEmailInToken(resolveAccessToken);
        String refreshTokenInRedis = redisUtil.getData(RT + emailInToken);
        if (refreshTokenInRedis == null) throw new CustomException(CustomResponseStatus.REFRESH_TOKEN_NOT_FOUND);

        redisUtil.deleteDate(RT + emailInToken);
        redisUtil.setData(resolveAccessToken, LOGOUT, jwtUtil.getExpiration(resolveAccessToken));
    }

    private Member forceJoin(OAuthInfoResponse joinParam) {
        Member joinMember = memberRepository.save(Member.from(joinParam));

        Role role = roleRepository.findByRoleType(RoleType.ROLE_MEMBER)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.ROLE_NOT_FOUND));

        memberRoleRepository.save(MemberRole.of(joinMember, role));

        return joinMember;
    }

    private Optional<Member> findMemberByOauthProviderAndProviderId(OAuthProvider provider, String providerId) {
        return memberRepository.findByProviderAndProviderId(provider, providerId);
    }
}
