package com.pettoyou.server.domains.store.repository.custom;

import com.pettoyou.server.domains.store.entity.StorePhoto;

import java.util.List;

public interface StorePhotoCustomRepository {
     List<StorePhoto> getStorePhotosOrderByPhotoOrder(Long storeId);
}
