package com.pettoyou.server.domains.hospital.service.auth;

import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
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

    @Override
    public void singUp(HospitalAdminSignUpReqDto signUpReqDto) {
        // Valid 체크
        // 1. 해당 username이 중복되지는 않는지
        Optional<HospitalAdmin> isValidUsername = hospitalAdminRepository.findByUsername(signUpReqDto.username());
        if(isValidUsername.isPresent()) {
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
        return null;
    }

    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
