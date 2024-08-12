package com.pettoyou.server.domains.hospital.dto.request;

import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record HospitalQueryCond(
        @Nullable List<Long> businessHourLabels, // 병원 태그에 대한 PK 리스트

        @Nullable List<Long> serviceLabels, // 병원 태그에 대한 PK 리스트

        @Nullable List<Long> specialtiesLabels, // 병원 태그에 대한 PK 리스트

        @Nullable List<Long> emergencyLabels, // 병원 태그에 대한 PK 리스트

//        @Nullable String filter, // 추천순, 리뷰 많은순, 평점 높은순
        @Nullable Integer radius, // 반경 조건을 추가할 때 사용 (기본은, 5000)
        @Nullable String openCond // 영업중인 병원 조회할 때 사용
) {
    public HospitalQueryCond {
        if (Objects.isNull(radius)) radius = 5000;
    }

    public List<Long> getTagIdList() {
        List<Long> tags = Stream.of(
                        businessHourLabels,
                        serviceLabels,
                        specialtiesLabels,
                        emergencyLabels)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        //nullable 하므로 Collectors.toList()

        if(tags.isEmpty()){
            return null;
        }
        return tags;

    }

}
