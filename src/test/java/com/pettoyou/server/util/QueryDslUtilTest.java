package com.pettoyou.server.util;

import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.review.service.ReviewService;
import com.pettoyou.server.domains.review.entity.Review;
import com.querydsl.core.types.OrderSpecifier;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.mockito.Mockito.mock;


@SpringBootTest
@Transactional
class QueryDslUtilTest {

    QueryDslUtil queryDslUtil;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    EntityManager em;

    private Review review;
    private Hospital hospital;
    @BeforeEach
    void setUp() {
        queryDslUtil = new QueryDslUtil();
        Hospital hospital = Hospital.builder()
                .storeName("name")
                .build();

        Review review = Review.builder()
                .reviewId(1L)
                .store(hospital)
                .rating(4)
                .memberId(1L)
                .build();



    }

    @Test
    void getOrderSpecifiers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at"), Sort.Order.desc("rating")));
        Class<?> entity = Review.class;
        // Extract Sort from Pageable
        Sort sort = pageable.getSort();



//        System.out.println(queryDslUtil.getOrderSpecifiers(sort, entity, column).stream().toArray(OrderSpecifier[]::new));
        OrderSpecifier[] array = queryDslUtil.getOrderSpecifiers(sort, entity).stream().toArray(OrderSpecifier[]::new);
        System.out.println("order is " + array[0].getOrder());
        System.out.println("target is " + array[0].getTarget());

    }

}