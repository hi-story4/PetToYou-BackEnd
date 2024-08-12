package com.pettoyou.server.domains.reserve.repository;

import com.pettoyou.server.domains.reserve.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}
