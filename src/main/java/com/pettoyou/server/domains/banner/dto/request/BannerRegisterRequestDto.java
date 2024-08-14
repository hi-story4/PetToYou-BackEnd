package com.pettoyou.server.domains.banner.dto.request;

import lombok.Builder;

@Builder
public record BannerRegisterRequestDto(String bannerName, String bannerLink) {}
