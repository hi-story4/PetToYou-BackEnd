package com.pettoyou.server.domains.banner.service.query;

import com.pettoyou.server.domains.banner.dto.response.BannerQueryRespDto;

import java.util.List;

public interface BannerQueryService {

    List<BannerQueryRespDto> queryBanners();
}
