package com.pettoyou.server.domains.store.dto.response;

import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.store.entity.StorePhoto;
import com.pettoyou.server.domains.store.entity.enums.StoreType;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StorePhotoDto(
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long storePhotoId,
        StoreType storeType,
        String storePhotoUrl,
        @Positive Integer photoOrder,
        BaseStatus photoStatus,
        Long storeId
) {
    public static StorePhotoDto toDto(StorePhoto storePhoto){

        return StorePhotoDto.builder()
                .createdAt(storePhoto.getCreatedAt())
                .modifiedAt(storePhoto.getModifiedAt())
                .storePhotoId(storePhoto.getStorePhotoId())
                .storePhotoUrl(storePhoto.getStorePhoto().getPhotoUrl())
                .photoOrder(storePhoto.getPhotoOrder())
                .photoStatus(storePhoto.getPhotoStatus())
                .storeId(storePhoto.getStore().getStoreId())
                .build();
    }
}
