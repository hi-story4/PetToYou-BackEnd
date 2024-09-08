package com.pettoyou.server.domains.review.dto;


import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.review.entity.Review;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.entity.enums.StoreType;

public record ReviewReqDto(String treatmentType,
                           String treatment,
                           Integer price,
                           Integer rating,
                           String content
)
{
    public static Review toEntity(Store store, Pet pet, Long userId, ReviewReqDto reviewReqDto, String storeType)
    {
        return Review.builder()
                .memberId(userId)
                .store(store)
                .pet(pet)
                .storeType(storeType)
                .rating(reviewReqDto.rating)
                .content(reviewReqDto.content)
                .treatmentType(reviewReqDto.treatmentType)
                .treatment(reviewReqDto.treatment)
                .price(reviewReqDto.price)
                .build();
//null 값 처리
    }

    public static Review toEntity(ReviewReqDto reviewReqDto){
        return Review.builder()
                .rating(reviewReqDto.rating)
                .content(reviewReqDto.content)
                .treatmentType(reviewReqDto.treatmentType)
                .treatment(reviewReqDto.treatment)
                .price(reviewReqDto.price)
                .build();
    }

}
