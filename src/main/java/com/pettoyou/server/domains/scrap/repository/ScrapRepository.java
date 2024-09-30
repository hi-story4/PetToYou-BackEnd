package com.pettoyou.server.domains.scrap.repository;

import com.pettoyou.server.domains.scrap.entity.Scrap;
import com.pettoyou.server.domains.scrap.repository.custom.ScrapCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long>, ScrapCustomRepository {

    boolean existsByMemberMemberIdAndStoreStoreId(Long memberId, Long storeId);
}
