package com.pettoyou.server.domains.banner.repository;

import com.pettoyou.server.domains.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
