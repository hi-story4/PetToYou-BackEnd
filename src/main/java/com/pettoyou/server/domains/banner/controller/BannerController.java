package com.pettoyou.server.domains.banner.controller;

import com.pettoyou.server.domains.banner.dto.response.BannerQueryRespDto;
import com.pettoyou.server.domains.banner.service.query.BannerQueryService;
import com.pettoyou.server.constant.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BannerController {
    private final BannerQueryService bannerQueryService;

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<List<BannerQueryRespDto>>> bannerQueryByBannerType() {
        List<BannerQueryRespDto> response = bannerQueryService.queryBanners();
        return ApiResponse.createSuccessWithOk(response);
    }
}
