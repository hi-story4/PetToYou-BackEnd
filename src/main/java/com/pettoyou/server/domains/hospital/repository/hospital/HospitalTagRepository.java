package com.pettoyou.server.domains.hospital.repository.hospital;

import com.pettoyou.server.domains.hospital.entity.hospital.HospitalTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalTagRepository extends JpaRepository<HospitalTag, Long> {
}