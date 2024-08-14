package com.pettoyou.server.domains.store.repository;

import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.repository.custom.StorePhotoCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long>, StorePhotoCustomRepository {



    @Query("SELECT s.storeName FROM Store s WHERE s.storeId = :storeId")
    String getStoreNameByStoreId(Long storeId);
}
