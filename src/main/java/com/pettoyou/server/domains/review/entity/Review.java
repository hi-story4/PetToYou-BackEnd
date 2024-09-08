package com.pettoyou.server.domains.review.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.review.dto.ReviewReqDto;
import com.pettoyou.server.domains.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE review SET review_status = 'DEACTIVATE' WHERE review_id=?")
@SQLRestriction("review_status = 'ACTIVATE'")
@Table(name = "review", indexes = {
        @Index(name = "idx_member_id", columnList = "memberId")
})
public class Review extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long reviewId;

    @NotNull
    private String storeType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BaseStatus reviewStatus;

    @Builder.Default
    @NotNull
    private Integer rating = 0;


    private String treatmentType;
    private String treatment;
    private Integer price;

    @NotNull
    private String content;

    @Builder.Default
    @NotNull
    private Integer pinned=0;

    //Index
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public static Double getRatingAvg(List<Review> reviews) {
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }


    public void modify(ReviewReqDto reviewDto){
        this.treatmentType = reviewDto.treatmentType();
        this.treatment = reviewDto.treatment();
        this.price = reviewDto.price();
        this.rating = reviewDto.rating();
        this.content=reviewDto.content();
    }
}
