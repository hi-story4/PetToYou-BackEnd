package com.pettoyou.server.domains.hospital.service.auth;

import com.pettoyou.server.config.jwt.util.JwtUtil;
import com.pettoyou.server.domains.auth.enums.TokenType;
import com.pettoyou.server.domains.auth.enums.TokenUserType;
import com.pettoyou.server.config.redis.util.RedisUtil;
import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.auth.AuthTokenGenerator;
import com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin.HospitalAdminSignInReqDto;
import com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin.HospitalAdminSignUpReqDto;
import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdmin;
import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdminRole;
import com.pettoyou.server.domains.hospital.repository.hospital.HospitalRepository;
import com.pettoyou.server.domains.hospital.repository.hospitalAdmin.HospitalAdminRepository;
import com.pettoyou.server.domains.hospital.repository.hospitalAdmin.HospitalAdminRoleRepository;
import com.pettoyou.server.domains.member.entity.Role;
import com.pettoyou.server.domains.member.entity.enums.RoleType;
import com.pettoyou.server.domains.member.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalAdminServiceImpl implements HospitalAdminService {
    private final HospitalAdminRepository hospitalAdminRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalAdminRoleRepository hospitalAdminRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final AuthTokenGenerator authTokenGenerator;

    private static final String RT = "RT:";
    private static final String LOGOUT = "LOGOUT";

    @Override
    public void singUp(HospitalAdminSignUpReqDto signUpReqDto) {
        // Valid 체크
        // 1. 해당 username이 중복되지는 않는지
        Optional<HospitalAdmin> isValidUsername = hospitalAdminRepository.findByUsername(signUpReqDto.username());
        if (isValidUsername.isPresent()) {
            throw new CustomException(CustomResponseStatus.USERNAME_ALREADY_EXIST);
        }

        // 2. 존재하는 HospitalId 인지
        Hospital existHospital = hospitalRepository.findByStoreId(signUpReqDto.hospitalId()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.HOSPITAL_NOT_FOUND)
        );

        HospitalAdmin hospitalAdmin = HospitalAdmin.of(
                signUpReqDto.username(),
                passwordEncoder.encode(signUpReqDto.password()),
                existHospital.getStoreId()
        );
        hospitalAdminRepository.save(hospitalAdmin);

        Role role = roleRepository.findByRoleType(RoleType.ROLE_HOSPITAL).orElseThrow(
                () -> new CustomException(CustomResponseStatus.ROLE_NOT_FOUND)
        );

        hospitalAdminRoleRepository.save(HospitalAdminRole.of(hospitalAdmin, role));
    }

    @Override
    public AuthTokens signIn(HospitalAdminSignInReqDto signInReqDto) {
        // 아이디가 일치하지 않는 경우
        HospitalAdmin hospitalAdmin = hospitalAdminRepository.findByUsername(signInReqDto.username()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.LOGIN_FAILED)
        );

        // 비밀번호가 일치하지 않는 경우
        if (!verifyPassword(signInReqDto.password(), hospitalAdmin.getPassword())) {
            throw new CustomException(CustomResponseStatus.LOGIN_FAILED);
        }

        List<RoleType> adminRoles = hospitalAdmin.getAllHospitalAdminRole();
        String refreshToken = redisUtil.getData(RT + hospitalAdmin.getUsername());
        if (refreshToken == null) {
            refreshToken = jwtUtil.createToken(hospitalAdmin.getUsername(), adminRoles, TokenType.REFRESH_TOKEN, TokenUserType.HOSPITAL_ADMIN_TOKEN);
            redisUtil.setData(RT + hospitalAdmin.getUsername(), refreshToken, jwtUtil.getExpiration(TokenType.REFRESH_TOKEN));
        }

        return authTokenGenerator.generateAdminToken(hospitalAdmin.getUsername(), adminRoles, refreshToken);
    }

    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
