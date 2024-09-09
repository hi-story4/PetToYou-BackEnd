package com.pettoyou.server.member.service.auth;

import com.pettoyou.server.domains.auth.AuthTokenGenerator;
import com.pettoyou.server.domains.auth.OAuthLoginParams;
import com.pettoyou.server.domains.auth.RequestOAuthInfoService;
import com.pettoyou.server.domains.auth.kakao.KakaoInfoResponse;
import com.pettoyou.server.domains.auth.kakao.KakaoLoginParam;
import com.pettoyou.server.domains.auth.naver.NaverInfoResponse;
import com.pettoyou.server.domains.auth.naver.NaverLoginParam;
import com.pettoyou.server.config.jwt.util.JwtUtil;
import com.pettoyou.server.config.jwt.util.TokenType;
import com.pettoyou.server.config.redis.util.RedisUtil;
import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.entity.MemberRole;
import com.pettoyou.server.domains.member.entity.Role;
import com.pettoyou.server.domains.member.entity.enums.MemberStatus;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import com.pettoyou.server.domains.member.entity.enums.RoleType;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.member.repository.MemberRoleRepository;
import com.pettoyou.server.domains.member.repository.RoleRepository;
import com.pettoyou.server.domains.member.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberRoleRepository memberRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RequestOAuthInfoService requestOAuthInfoService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @InjectMocks
    private AuthServiceImpl authService;

    /***
     * 소셜 로그인 테스트
     */

    @Test
    void 기존_회원_정상_카카오_로그인_리프레시_토큰이_존재하는_경우() {
        // given
        Member member = createMember();
        KakaoInfoResponse kakaoInfoResponse = createKakaoInfoResponse();
        AuthTokens authTokens = createAuthTokens();
        KakaoLoginParam kakaoLoginParam = createKakaoLoginParam();

        when(memberRepository.findByProviderAndProviderId(any(OAuthProvider.class), anyString()))
                .thenReturn(Optional.ofNullable(member));
        when(redisUtil.getData(anyString())).thenReturn("baseRT");
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);
        when(requestOAuthInfoService.request(any(OAuthLoginParams.class))).thenReturn(kakaoInfoResponse);

        // when
        AuthTokens resultToken = authService.signIn(kakaoLoginParam);

        // then
        assertThat(resultToken.accessToken()).isEqualTo(authTokens.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(authTokens.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(authTokens.exprTime());
    }

    @Test
    void 기존_회원_정상_카카오_로그인_리프레시_토큰이_존재하지_않는_경우() {
        // given
        Member member = createMember();
        KakaoInfoResponse kakaoInfoResponse = createKakaoInfoResponse();
        AuthTokens authTokens = createAuthTokens();
        KakaoLoginParam kakaoLoginParam = createKakaoLoginParam();

        when(memberRepository.findByProviderAndProviderId(any(OAuthProvider.class), anyString()))
                .thenReturn(Optional.ofNullable(member));
        when(requestOAuthInfoService.request(any(OAuthLoginParams.class))).thenReturn(kakaoInfoResponse);
        when(redisUtil.getData(anyString())).thenReturn(null);
        when(jwtUtil.getExpiration(any(TokenType.class))).thenReturn(authTokens.exprTime());
        when(jwtUtil.createMemberToken(anyString(), anyList(), any(TokenType.class))).thenReturn(authTokens.refreshToken());
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);

        // when
        AuthTokens resultToken = authService.signIn(kakaoLoginParam);

        // then
        verify(redisUtil, times(1)).setData(anyString(), anyString(), anyLong());

        assertThat(resultToken.accessToken()).isEqualTo(authTokens.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(authTokens.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(authTokens.exprTime());
    }

    @Test
    void 기존_회원_정상_네이버_로그인_리프레시_토큰이_존재하는_경우() {
        // given
        Member member = createMember();
        NaverInfoResponse naverInfoResponse = createNaverInfoResponse();
        AuthTokens authTokens = createAuthTokens();
        NaverLoginParam naverLoginParam = createNaverLoginParam();

        when(memberRepository.findByProviderAndProviderId(any(OAuthProvider.class), anyString()))
                .thenReturn(Optional.ofNullable(member));
        when(redisUtil.getData(anyString())).thenReturn("baseRT");
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);
        when(requestOAuthInfoService.request(any(OAuthLoginParams.class))).thenReturn(naverInfoResponse);

        // when
        AuthTokens resultToken = authService.signIn(naverLoginParam);

        // then
        assertThat(resultToken.accessToken()).isEqualTo(authTokens.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(authTokens.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(authTokens.exprTime());
    }

    @Test
    void 기존_회원_정상_네이버_로그인_리프레시_토큰이_존재하지_않는_경우() {
        // given
        Member member = createMember();
        NaverInfoResponse naverInfoResponse = createNaverInfoResponse();
        AuthTokens authTokens = createAuthTokens();
        NaverLoginParam naverLoginParam = createNaverLoginParam();

        when(memberRepository.findByProviderAndProviderId(any(OAuthProvider.class), anyString()))
                .thenReturn(Optional.ofNullable(member));
        when(redisUtil.getData(anyString())).thenReturn(null);
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);
        when(requestOAuthInfoService.request(any(OAuthLoginParams.class))).thenReturn(naverInfoResponse);
        when(jwtUtil.getExpiration(any(TokenType.class))).thenReturn(authTokens.exprTime());
        when(jwtUtil.createMemberToken(anyString(), anyList(), any(TokenType.class))).thenReturn(authTokens.refreshToken());
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);

        // when
        AuthTokens resultToken = authService.signIn(naverLoginParam);

        // then
        verify(redisUtil, times(1)).setData(anyString(), anyString(), anyLong());

        assertThat(resultToken.accessToken()).isEqualTo(authTokens.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(authTokens.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(authTokens.exprTime());
    }

    @Test
    void 신규_회원_카카오_로그인시_강제_회원가입_진행() {
        // given
        KakaoLoginParam kakaoLoginParam = createKakaoLoginParam();
        Member member = createMember();
        Role role = createRole();
        KakaoInfoResponse kakaoInfoResponse = createKakaoInfoResponse();
        AuthTokens authTokens = createAuthTokens();

        when(memberRepository.findByProviderAndProviderId(any(OAuthProvider.class), anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        when(roleRepository.findByRoleType(any(RoleType.class))).thenReturn(Optional.of(role));
        when(memberRoleRepository.save(any(MemberRole.class))).thenReturn(null);

        when(redisUtil.getData(anyString())).thenReturn("baseRt");
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);
        when(requestOAuthInfoService.request(any(OAuthLoginParams.class))).thenReturn(kakaoInfoResponse);

        // when
        AuthTokens resultToken = authService.signIn(kakaoLoginParam);

        // then
        assertThat(resultToken.accessToken()).isEqualTo(authTokens.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(authTokens.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(authTokens.exprTime());
    }

    @Test
    void 신규_회원_네이버_로그인시_강제_회원가입_진행() {
        // given
        NaverLoginParam naverLoginParam = createNaverLoginParam();
        Member member = createMember();
        Role role = createRole();
        NaverInfoResponse naverInfoResponse = createNaverInfoResponse();
        AuthTokens authTokens = createAuthTokens();

        when(memberRepository.findByProviderAndProviderId(any(OAuthProvider.class), anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        when(roleRepository.findByRoleType(any(RoleType.class))).thenReturn(Optional.of(role));
        when(memberRoleRepository.save(any(MemberRole.class))).thenReturn(null);

        when(redisUtil.getData(anyString())).thenReturn("baseRt");
        when(authTokenGenerator.generate(anyString(), anyList(), anyString())).thenReturn(authTokens);
        when(requestOAuthInfoService.request(any(OAuthLoginParams.class))).thenReturn(naverInfoResponse);

        // when
        AuthTokens resultToken = authService.signIn(naverLoginParam);

        // then
        assertThat(resultToken.accessToken()).isEqualTo(authTokens.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(authTokens.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(authTokens.exprTime());
    }

    /***
     * 토큰 재발급
     */

    @Test
    void 정상_토큰_재발급() {
        // given
        String validRefreshToken = "validRefreshToken";
        String emailInToken = "test@gmail.com";
        Member member = createMember();
        AuthTokens generateToken = createAuthTokens();

        when(jwtUtil.resolveToken(anyString())).thenReturn(validRefreshToken);
        when(jwtUtil.getEmailInToken(anyString())).thenReturn(emailInToken);
        when(redisUtil.getData(anyString())).thenReturn(validRefreshToken);
        when(authTokenGenerator.generate(anyString(), anyList())).thenReturn(generateToken);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(member));

        // when
        AuthTokens resultToken = authService.reissue(validRefreshToken);

        // then
        assertThat(resultToken.accessToken()).isEqualTo(generateToken.accessToken());
        assertThat(resultToken.refreshToken()).isEqualTo(generateToken.refreshToken());
        assertThat(resultToken.exprTime()).isEqualTo(generateToken.exprTime());
    }

    @Test
    void 재발급_요청시_토큰이_매칭되지_않으면_예외발생() {
        // given
        String validRefreshToken = "validRefreshToken";
        String wrongRefreshToken = "wrongRefreshToken";
        String emailInToken = "test@gmail.com";

        when(jwtUtil.resolveToken(anyString())).thenReturn(validRefreshToken);
        when(jwtUtil.getEmailInToken(anyString())).thenReturn(emailInToken);
        when(redisUtil.getData(anyString())).thenReturn(wrongRefreshToken);

        // when
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> authService.reissue(validRefreshToken))
                .withMessage(CustomResponseStatus.REFRESH_TOKEN_NOT_MATCH.getMessage());
    }

    /***
     * 로그아웃
     */

    @Test
    void 정상_로그아웃() {
        // given
        String validAccessToken = "validAccessToken";
        String emailInToken = "test@gmail.com";
        String validRefreshTokenInReds = "validRefreshToken";

        when(jwtUtil.resolveToken(anyString())).thenReturn(validAccessToken);
        when(jwtUtil.getEmailInToken(anyString())).thenReturn(emailInToken);
        when(redisUtil.getData(anyString())).thenReturn(validRefreshTokenInReds);

        // when
        authService.logout(validAccessToken);

        // then
        verify(redisUtil, times(1)).deleteDate(anyString());
        verify(redisUtil, times(1)).setData(anyString(), anyString(), anyLong());
    }

    @Test
    void 레디스에_리프레시_토큰이_없는_경우_예외발생() {
        // given
        String validAccessToken = "validAccessToken";
        String emailInToken = "test@gmail.com";

        when(jwtUtil.resolveToken(anyString())).thenReturn(validAccessToken);
        when(jwtUtil.getEmailInToken(anyString())).thenReturn(emailInToken);
        when(redisUtil.getData(anyString())).thenReturn(null);

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> authService.logout(validAccessToken))
                .withMessage(CustomResponseStatus.REFRESH_TOKEN_NOT_FOUND.getMessage());
    }

    private KakaoLoginParam createKakaoLoginParam() {
        return KakaoLoginParam.from("authorizationCode");
    }

    private NaverLoginParam createNaverLoginParam() {
        return NaverLoginParam.of("authorizationCode", "state");
    }

    private AuthTokens createAuthTokens() {
        return AuthTokens.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .exprTime(3000L)
                .build();
    }

    private Role createRole() {
        return Role.builder()
                .roleId(1L)
                .roleType(RoleType.ROLE_MEMBER)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .memberId(1L)
                .name("성춘")
                .nickName("choon")
                .phone("010-7592-0693")
                .email("test@naver.com")
                .provider(OAuthProvider.KAKAO)
                .providerId("3535")
                .memberStatus(MemberStatus.ACTIVATE)
                .build();
    }

    private KakaoInfoResponse createKakaoInfoResponse() {
        return KakaoInfoResponse.builder()
                .id("123")
                .kakaoAccount(
                        KakaoInfoResponse.KakaoAccount.builder()
                                .profile(
                                        KakaoInfoResponse.KakaoProfile.builder()
                                                .nickname("choon")
                                                .build()
                                )
                                .email("test@naver.com")
                                .name("홍길동")
                                .phone_number("+82 10-2222-2222")
                                .build()
                )
                .build();
    }

    private NaverInfoResponse createNaverInfoResponse() {
        return NaverInfoResponse.builder()
                .response(
                        NaverInfoResponse.Response.builder()
                                .id("123")
                                .name("홍길동")
                                .nickname("홍길동")
                                .email("test@naver.com")
                                .mobile("010-1111-1111")
                                .build()
                )
                .build();
    }
}