package com.pettoyou.server.domains.hospital.repository.hospitalAdmin;

import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalAdminRepository extends JpaRepository<HospitalAdmin, Long> {
    Optional<HospitalAdmin> findByUsername(String username);
}
