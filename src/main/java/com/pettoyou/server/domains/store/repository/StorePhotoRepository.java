package com.pettoyou.server.domains.store.repository;

import com.pettoyou.server.domains.store.entity.StorePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorePhotoRepository extends JpaRepository<StorePhoto, Long> {

}