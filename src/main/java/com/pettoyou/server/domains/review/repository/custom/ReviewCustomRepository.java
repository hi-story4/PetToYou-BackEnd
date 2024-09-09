package com.pettoyou.server.domains.review.repository.custom;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {
    Page<Tuple>findReviewsFetchJoinPetsByStoreId(Long StoreId, Pageable pageable);

    long updatePinned(Long reviewId, Integer pinned);
}
