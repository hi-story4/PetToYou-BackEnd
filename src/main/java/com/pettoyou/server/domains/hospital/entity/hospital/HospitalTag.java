package com.pettoyou.server.domains.hospital.entity.hospital;

import com.pettoyou.server.domains.hospital.entity.enums.HospitalTagType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "hospital_tag")
public class HospitalTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalTagId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HospitalTagType tagType;

    // Service, BusinessHour, Specialities, Emergency
    @NotNull
    private String tagContent;

    public static List<TagMapper> toEntity(Hospital hospital, List<HospitalTag> tags) {

        if (tags.isEmpty()) {
            throw new IllegalArgumentException("No tags");
        }
        return tags.stream().map(tag -> TagMapper.builder()
                .hospitalTag(tag)
                .hospital(hospital)
                .build()
        ).toList();
        //collect.toList와 차이점은 null 일 경우 throw exception

    }
}