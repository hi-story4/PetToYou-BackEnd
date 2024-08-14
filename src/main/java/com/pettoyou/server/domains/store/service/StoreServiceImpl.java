package com.pettoyou.server.domains.store.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.store.dto.response.StorePhotoDto;
import com.pettoyou.server.domains.store.entity.StorePhoto;
import com.pettoyou.server.domains.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    //store photo
    public List<StorePhotoDto> getAllStorePhoto(Long storeId) {
        List<StorePhoto> photos = storeRepository.getStorePhotosOrderByPhotoOrder(storeId);
        if (photos==null || photos.isEmpty()) {
            throw new CustomException(CustomResponseStatus.STORE_PHOTO_NOT_FOUND);
        }
        return photos.stream().map(StorePhotoDto::toDto).toList();
    }
}
