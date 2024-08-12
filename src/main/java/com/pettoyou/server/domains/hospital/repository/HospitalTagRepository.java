package com.pettoyou.server.domains.hospital.repository;

import com.pettoyou.server.domains.hospital.entity.HospitalTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalTagRepository extends JpaRepository<HospitalTag, Long> {
}