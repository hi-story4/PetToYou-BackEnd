package com.pettoyou.server.domains.hospital.dto.response;

import com.pettoyou.server.domains.hospital.entity.HospitalTag;
import com.pettoyou.server.domains.hospital.dto.HospitalTagDto;
import lombok.Builder;

import java.util.List;

@Builder
public record HospitalDtoWithDistance(
        Long storeId,
        String storeName,
        String thumbnailUrl,
        Times time,
        Long reviewCount,
        Double ratingAvg,
        String distance,
        HospitalTagDto tags
) {
    public static HospitalDtoWithDistance of(
            Long storeId,
            String storeName,
            String thumbnailUrl,
            Long reviewCount,
            Double ratingAvg,
            Times time,
            List<HospitalTag> hospitalTags,
            double distance
    ) {
        return HospitalDtoWithDistance.builder()
                .storeId(storeId)
                .storeName(storeName)
                .thumbnailUrl(thumbnailUrl == null ? "test.jpg" : thumbnailUrl)
                .time(time)
                .reviewCount(reviewCount == null ? 5L : reviewCount)
                .ratingAvg(ratingAvg == null ? 4.5 : ratingAvg)
                .distance(distanceFormatting(distance))
                .tags(HospitalTagDto.toDto(hospitalTags))
                .build();
    }

    private static String distanceFormatting(double distance) {
        return String.format("%.2f", distance / 1000.0);
    }

}
