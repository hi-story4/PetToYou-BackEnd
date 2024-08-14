package com.pettoyou.server.domains.scrap.dto.response;

import com.pettoyou.server.domains.store.entity.Address;
import lombok.Builder;

@Builder
public record ScrapQueryRespDto(
        Long scrapId,
        String thumbnailUrl,
        String storeName,
        String address
) {
    public ScrapQueryRespDto(Long scrapId, String thumbnailUrl, String storeName, Address address) {
        this(scrapId, thumbnailUrl, storeName, address.generateDefaultAddressFormat());
    }
}
