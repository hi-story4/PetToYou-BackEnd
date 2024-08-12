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
        String eupmyun) {

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .sido(address.getSido())
                .eupmyun(address.getEupmyun())
                .sigungu(address.getSigungu())
                .build();
    }
}