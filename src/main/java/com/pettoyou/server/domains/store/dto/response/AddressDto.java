package com.pettoyou.server.domains.store.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pettoyou.server.domains.store.entity.Address;
import lombok.Builder;

/**
 * DTO for {@link Address}
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressDto(
        String sido,
        String sigungu,
        String buildingNumber) {

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .sido(address.getSido())
                .buildingNumber(address.getBuildingNumber())
                .sigungu(address.getSigungu())
                .build();
    }
}