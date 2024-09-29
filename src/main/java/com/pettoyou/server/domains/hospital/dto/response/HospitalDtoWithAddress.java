package com.pettoyou.server.domains.hospital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.hospital.entity.hospital.HospitalTag;
import com.pettoyou.server.domains.hospital.dto.HospitalTagDto;
import com.pettoyou.server.domains.store.dto.response.AddressDto;
import com.pettoyou.server.domains.store.entity.Address;
import com.pettoyou.server.domains.store.entity.BusinessHour;
import com.pettoyou.server.domains.store.entity.enums.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

/**
 * DTO for {@link Hospital}
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record HospitalDtoWithAddress(
        @NotNull Long storeId,
        @NotNull @Size(min = 2) String storeName,
        String thumbnailUrl,
        AddressDto addressDto,

        Times time,
        HospitalTagDto tags,
        SubscriptionStatus subscriptionStatus
) {
    public static HospitalDtoWithAddress of(
            Long storeId,
            String storeName,
            String thumbnailUrl,
            Address address,
            BusinessHour businessHour,
            List<HospitalTag> tags,
            SubscriptionStatus subscriptionStatus
    ) {

        return HospitalDtoWithAddress.builder()
                .storeId(storeId)
                .storeName(storeName)
                .thumbnailUrl(thumbnailUrl)
                .addressDto(AddressDto.toDto(address))
                .time(businessHour != null ? Times.of(businessHour) : null)
                .tags(HospitalTagDto.toDto(tags))
                .subscriptionStatus(subscriptionStatus)
                .build();
    }
}