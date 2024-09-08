package com.pettoyou.server.domains.review.repository.custom;

import com.pettoyou.server.domains.review.dto.ReviewRespDto;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {
    Page<ReviewRespDto>findReviewsFetchJoinPetsByStoreId(Long storeId, Pageable pageable);

    long updatePinned(Long reviewId, Integer pinned);
}
