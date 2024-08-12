package com.pettoyou.server.domains.hospital.repository;

import com.pettoyou.server.domains.hospital.entity.TagMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagMapperRepository extends JpaRepository<TagMapper, Long> {
}