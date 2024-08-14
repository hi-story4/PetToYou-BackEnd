package com.pettoyou.server.domains.review.dto;

import com.querydsl.core.Tuple;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.pettoyou.server.domains.review.entity.QReview.review;

/**
 * DTO for {@link com.pettoyou.server.domains.review.entity.Review}
 */

@Builder
public record ReviewRespDto(@PastOrPresent LocalDateTime createdAt, @PastOrPresent LocalDateTime modifiedAt, Long reviewId,
                            @NotNull Integer rating, String treatment, Integer price, String content,
                            @NotNull Long memberId, String petName, String species,
                            LocalDate birth) implements Serializable {
    public static ReviewRespDto toDto(Tuple tuple) {
    ReviewRespDtoBuilder reviewRespDto =  ReviewRespDto.builder()
            .reviewId(tuple.get(review.reviewId))
            .createdAt(tuple.get(review.createdAt))
            .rating(tuple.get(review.rating))
            .treatment(tuple.get(review.treatment))
            .price(tuple.get(review.price))
            .content(tuple.get(review.content))
            .memberId(tuple.get(review.memberId))
            .petName(tuple.get(pet.petName))
            .species(tuple.get(pet.species))
            .birth(tuple.get(pet.birth));

    if(tuple.get(review.modifiedAt)!=null){
        reviewRespDto.modifiedAt(tuple.get(review.modifiedAt));
    }

    return reviewRespDto.build();

}
}