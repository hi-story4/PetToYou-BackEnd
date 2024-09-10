package com.pettoyou.server.domains.review.dto;

import com.pettoyou.server.domains.pet.entity.enums.Species;
import com.pettoyou.server.domains.review.entity.Review;
import com.querydsl.core.Tuple;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.pettoyou.server.domains.pet.entity.QPet.pet;
import static com.pettoyou.server.domains.review.entity.QReview.review;

/**
 * DTO for {@link com.pettoyou.server.domains.review.entity.Review}
 */

@Builder
public record ReviewRespDto(@PastOrPresent LocalDateTime createdAt, @PastOrPresent LocalDateTime modifiedAt, Long reviewId,
                            @NotNull Integer rating, String treatment, String treatmentType, Integer price, String content,
                            @NotNull Long memberId, String petName, Species species,
                            LocalDate birth, Integer pinned) implements Serializable {
    public static ReviewRespDto toDto(Review review, String petName, Species species, LocalDate birth) {
    return ReviewRespDto.builder()
            .reviewId(review.getReviewId())
            .createdAt(review.getCreatedAt())
            .rating(review.getRating())
            .treatment(review.getTreatment())
            .treatmentType(review.getTreatmentType())
            .price(review.getPrice())
            .content(review.getContent())
            .memberId(review.getMemberId())
            .pinned(review.getPinned())
            .petName(petName)
            .species(species)
            .birth(birth)
            .modifiedAt(review.getModifiedAt())
            .build();

}}