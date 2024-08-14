package com.pettoyou.server.domains.store.service;

import com.pettoyou.server.domains.store.dto.response.StorePhotoDto;

import java.util.List;

public interface StoreService {
    List<StorePhotoDto> getAllStorePhoto(Long storeId);
}
