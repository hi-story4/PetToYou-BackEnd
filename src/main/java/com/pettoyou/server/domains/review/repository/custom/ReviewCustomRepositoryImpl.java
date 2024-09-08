package com.pettoyou.server.domains.review.repository.custom;

import com.pettoyou.server.domains.review.dto.ReviewRespDto;
import com.pettoyou.server.domains.review.entity.Review;
import com.pettoyou.server.util.QueryDslUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pettoyou.server.domains.pet.entity.QPet.pet;
import static com.pettoyou.server.domains.review.entity.QReview.review;

@Repository
@Slf4j
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {


    private final JPAQueryFactory jpaQueryFactory;
    private final QueryDslUtil queryDslUtil;

    public ReviewCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory, QueryDslUtil queryDslUtil) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.queryDslUtil = queryDslUtil;
    }

    public Page<ReviewRespDto> findReviewsFetchJoinPetsByStoreId(Long storeId, Pageable pageable)
    {
        //기본 order (상단 고정 기능)
        OrderSpecifier<?> pinnedOrder = review.pinned.desc();
        //Pageable.Sort
        OrderSpecifier<?>[] pageableOrder = queryDslUtil.getOrderSpecifiers(pageable.getSort(), Review.class).stream().toArray(OrderSpecifier[]::new);
        //결합된 Sort 조건.
        OrderSpecifier<?>[] combinedOrder = Stream.concat(Stream.of(pinnedOrder), Stream.of(pageableOrder))
                .toArray(OrderSpecifier[]::new);


        List<Tuple> results = jpaQueryFactory.select(review, pet.petName, pet.birth, pet.species)
                .from(review)
                .join(review.pet, pet).fetchJoin()
                .where(review.store.storeId.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(combinedOrder)
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(review.count())
                .from(review)
                .join(review.pet, pet).fetchJoin()
                .where(review.store.storeId.eq(storeId));


        List<ReviewRespDto> content = results.stream()
                .map(result -> ReviewRespDto
                        .toDto(result.get(review), result.get(pet.petName), result.get(pet.species),result.get(pet.birth)))
                .collect(Collectors.toList());



        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public long updatePinned(Long reviewId, Integer pinned)
    {
        long result = jpaQueryFactory.update(review)
                .set(review.pinned, pinned)
                .where(review.reviewId.eq(reviewId))
                .execute();
        return result;

    }
}

