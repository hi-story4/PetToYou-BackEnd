package com.pettoyou.server.domains.hospital.repository.custom;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDtoWithAddress;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDtoWithDistance;
import com.pettoyou.server.domains.hospital.entity.HospitalTag;
import com.pettoyou.server.domains.hospital.dto.request.HospitalQueryCond;
import com.pettoyou.server.domains.hospital.dto.request.HosptialSearchQueryInfo;
import com.pettoyou.server.domains.hospital.dto.response.Times;
import com.pettoyou.server.domains.hospital.entity.QHospital;
import com.pettoyou.server.domains.hospital.entity.QHospitalTag;
import com.pettoyou.server.domains.hospital.entity.QTagMapper;
import com.pettoyou.server.domains.store.entity.Address;
import com.pettoyou.server.domains.store.entity.BusinessHour;
import com.pettoyou.server.domains.store.entity.QBusinessHour;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.pettoyou.server.domains.hospital.entity.QHospital.*;
import static com.pettoyou.server.domains.hospital.entity.QHospitalTag.hospitalTag;
import static com.pettoyou.server.domains.hospital.entity.QTagMapper.*;
import static com.pettoyou.server.domains.review.entity.QReview.review;
import static com.pettoyou.server.domains.store.entity.QBusinessHour.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HospitalCustomRepositoryImpl implements HospitalCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<HospitalDtoWithDistance> findHospitalOptimization(
            Pageable pageable,
            int dayOfWeek, String point, LocalTime now, HospitalQueryCond queryCond
    ) {
        NumberPath<Double> distanceAlias = Expressions.numberPath(Double.class, "distance");
        NumberPath<Long> reviewCount = Expressions.numberPath(Long.class, "reviewCount");
        NumberPath<Double> ratingAvg = Expressions.numberPath(Double.class, "ratingAvg");

        // 1. 필터 조건에 부합하는 병원 가져오기
        List<Tuple> hospitals = jpaQueryFactory
                .select(
                        hospital.storeId,
                        hospital.storeName,
                        hospital.thumbnail.photoUrl,
                        businessHour,
                        review.countDistinct().as(reviewCount),
                        review.rating.avg().as(ratingAvg),
                        Expressions.stringTemplate(
                                "ST_Distance_Sphere(ST_PointFromText({0}, 4326), {1})",
                                point, hospital.address.point
                        ).as("distance"))
                .from(hospital)
                .leftJoin(hospital.businessHours, businessHour)
                .on(businessHour.dayOfWeek.eq(dayOfWeek))
                .leftJoin(hospital.reviews, review)
                .where(
                        inDistance(point, queryCond.radius()),
                        hospitalTagsEqSubQuery(queryCond.getTagIdList()),
                        openHospitalSubQuery(queryCond.openCond(), Time.valueOf(now), dayOfWeek)
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(distanceAlias.asc().nullsLast())
                .groupBy(
                        hospital,
                        businessHour
                )
                .fetch();

        // 2. 각 병원에서 가지고있는 HospitalTag를 Map에 담기
        List<Long> hospitalIds = hospitals.stream()
                .map(t -> t.get(hospital.storeId))
                .toList();

        Map<Long, List<HospitalTag>> hospitalTagMap = jpaQueryFactory
                .select(
                        tagMapper.hospital.storeId,
                        tagMapper.hospitalTag
                )
                .from(tagMapper)
                .where(tagMapper.hospital.storeId.in(hospitalIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(tagMapper.hospital.storeId),
                        Collectors.mapping(tuple -> tuple.get(tagMapper.hospitalTag), Collectors.toList())
                ));

        // 3. 페이징을 위한 조회된 병원의 전체 개수 구하는 쿼리
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(hospital.count())
                .from(hospital)
                .where(
                        inDistance(point, queryCond.radius()),
                        hospitalTagsEqSubQuery(queryCond.getTagIdList()),
                        openHospitalSubQuery(queryCond.openCond(), Time.valueOf(now), dayOfWeek)
                );


        if (countQuery == null) throw new CustomException(CustomResponseStatus.STORE_NOT_FOUND);

        List<HospitalDtoWithDistance> result = hospitals.stream()
                .map(t -> {
                    Long storeId = t.get(hospital.storeId);
                    String storeName = t.get(hospital.storeName);
                    String thumbnailUrl = t.get(hospital.thumbnail.photoUrl);
                    Long reviewCnt = t.get(reviewCount);
                    Double ratingAverage = t.get(ratingAvg);
                    Double distance = Optional.ofNullable(t.get(distanceAlias)).orElse(Double.MAX_VALUE);
                    BusinessHour storeBusinessHour = t.get(businessHour);
                    Times times = storeBusinessHour != null ? Times.of(storeBusinessHour) : null;

                    return HospitalDtoWithDistance.of(
                            storeId,
                            storeName,
                            thumbnailUrl,
                            reviewCnt,
                            ratingAverage,
                            times,
                            hospitalTagMap.getOrDefault(storeId, new ArrayList<>()),
                            distance
                    );
                })
                .toList();

        log.info(pageable.toString(), "page");
        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<HospitalDtoWithAddress> findHospitalBySearch(Pageable pageable, HosptialSearchQueryInfo queryInfo, Integer dayOfWeek) {

        List<Tuple> hospitals = jpaQueryFactory
                .select(
                        hospital.storeId, hospital.storeName,
                        hospital.thumbnail.photoUrl.as("thumbnailUrl"), hospital.address,
                        businessHour)
                .from(hospital)
                .leftJoin(hospital.businessHours, businessHour)
                .on(businessHour.dayOfWeek.eq(dayOfWeek))
                .where(hospital.storeName.contains(queryInfo.storeName()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.
                select(hospital.count())
                .from(hospital)
                .where(hospital.storeName.contains(queryInfo.storeName()));
//                .fetchOne();


        // 2. 각 병원에서 가지고있는 HospitalTag를 Map에 담기
        List<Long> hospitalIds = hospitals.stream()
                .map(t -> t.get(hospital.storeId))
                .toList();

        Map<Long, List<HospitalTag>> hospitalTagMap = jpaQueryFactory
                .select(
                        tagMapper.hospital.storeId,
                        tagMapper.hospitalTag
                )
                .from(tagMapper)
                .where(tagMapper.hospital.storeId.in(hospitalIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(tagMapper.hospital.storeId),
                        Collectors.mapping(tuple -> tuple.get(tagMapper.hospitalTag), Collectors.toList())
                ));


        if (countQuery == null) throw new CustomException(CustomResponseStatus.STORE_NOT_FOUND);

        List<HospitalDtoWithAddress> content = hospitals.stream()
                .map(t -> {
                    Long storeId = t.get(hospital.storeId);
                    String storeName = t.get(hospital.storeName);
                    String thumbnailUrl = t.get(hospital.thumbnail.photoUrl);
                    Address address = t.get(hospital.address);
                    BusinessHour storeBusinessHour = t.get(businessHour);

                    return HospitalDtoWithAddress.of(
                            storeId,
                            storeName,
                            thumbnailUrl,
                            address,
                            storeBusinessHour,
                            hospitalTagMap.getOrDefault(storeId, new ArrayList<>())
                    );
                })
                .toList();

        if (content == null) {
            throw new CustomException(CustomResponseStatus.HOSPITAL_NOT_FOUND);
        }
        //페이징 최적화
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    @Override
    public List<HospitalTag> findTagList(Long hospitalId) {
        return jpaQueryFactory
                .select(hospitalTag)
                .from(tagMapper)
                .join(tagMapper.hospitalTag, hospitalTag)
                .where(tagMapper.hospital.storeId.eq(hospitalId))
                .fetch();
    }


    private BooleanExpression hospitalTagsEqSubQuery(List<Long> tagsCond) {
        // BusinessHour, Specialities, Emergency 모두 여기서 처리됨.
        return tagsCond != null
                ? hospital.storeId.in(
                JPAExpressions
                        .select(tagMapper.hospital.storeId)
                        .from(tagMapper)
                        .where(tagMapper.hospitalTag.hospitalTagId.in(tagsCond)))
                : null;
    }

    private BooleanExpression openHospitalSubQuery(String openCond, Time now, int dayOfWeek) {
        return openCond != null
                ? hospital.storeId.in(
                JPAExpressions
                        .select(businessHour.store.storeId)
                        .from(businessHour)
                        .where(
                                businessHour.dayOfWeek.eq(dayOfWeek)
                                        .and(businessHour.startTime.isNotNull())
                                        .and(businessHour.endTime.isNotNull())
                                        .and(businessHour.startTime.loe(now))
                                        .and(businessHour.endTime.goe(now))
                        )
        )
                : null;
    }

    private BooleanExpression inDistance(String point, Integer radius) {
        return radius != null
                ? Expressions.booleanTemplate(
                "ST_Contains(ST_Buffer(ST_PointFromText({0}, 4326), {1}), {2})",
                point, radius, hospital.address.point)
                : null;
    }
}
