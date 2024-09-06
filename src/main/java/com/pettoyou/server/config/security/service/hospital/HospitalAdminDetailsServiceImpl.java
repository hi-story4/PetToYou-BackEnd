package com.pettoyou.server.config.security.service.hospital;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdmin;
import com.pettoyou.server.domains.hospital.repository.hospitalAdmin.HospitalAdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class HospitalAdminDetailsServiceImpl implements UserDetailsService {
    private final HospitalAdminRepository hospitalAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HospitalAdmin hospitalAdmin = hospitalAdminRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));

        // hospitalAdmin의 roles를 영속성 컨텍스트에 담는 것
        Hibernate.initialize(hospitalAdmin.getRoles());

        return new HospitalAdminDetails(hospitalAdmin);
    }
}
