package com.pettoyou.server.domains.hospital.dto;


import com.pettoyou.server.domains.hospital.entity.HospitalTag;
import com.pettoyou.server.domains.hospital.entity.enums.HospitalTagType;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;

import static com.pettoyou.server.domains.hospital.entity.enums.HospitalTagType.*;

@Builder
public record HospitalTagDto(
        //Response
        List<String> services,
        List<String> businessHours,
        List<String> specialities,
        List<String> emergency) {


    public static HospitalTagDto toDto(List<HospitalTag> tags) {
        Map<HospitalTagType, List<String>> result =
                Optional.ofNullable(tags)
                        .orElse(Collections.emptyList())
                        .stream()
                        .collect(Collectors.groupingBy(HospitalTag::getTagType
                                , Collectors.mapping(HospitalTag::getTagContent, Collectors.toList())
                        ));

        return HospitalTagDto.builder()
                .services(result.getOrDefault(SERVICE, new ArrayList<>()))
                .businessHours(result.getOrDefault(BUSINESSHOUR, new ArrayList<>()))
                .specialities(result.getOrDefault(SPECIALITIES, new ArrayList<>()))
                .emergency(result.getOrDefault(EMERGENCY, new ArrayList<>()))
                .build();
    }




}