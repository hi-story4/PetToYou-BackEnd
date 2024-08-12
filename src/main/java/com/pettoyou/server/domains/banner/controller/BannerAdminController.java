package com.pettoyou.server.domains.banner.controller;

import com.pettoyou.server.domains.banner.dto.request.BannerRegisterRequestDto;
import com.pettoyou.server.domains.banner.dto.response.BannerRegisterResponseDto;
import com.pettoyou.server.domains.banner.service.BannerService;
import com.pettoyou.server.constant.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class BannerAdminController {
    private final BannerService bannerService;

    // 배너 등록
    @PostMapping("/banner")
    public ResponseEntity<ApiResponse<BannerRegisterResponseDto>> bannerRegister(
            @RequestPart(value = "bannerRequestDto") BannerRegisterRequestDto bannerRegisterRequestDto,
            @RequestPart(value = "bannerImg") MultipartFile bannerImg
    ) {

        BannerRegisterResponseDto response = bannerService.bannerRegister(bannerRegisterRequestDto, bannerImg);
        return ApiResponse.createSuccessWithOk(response);
    }

    // 배너 수정
    @PutMapping("/banner/{bannerId}")
    public ResponseEntity<ApiResponse<BannerRegisterResponseDto>> bannerModify(
            @RequestPart(value = "bannerRequestDto") BannerRegisterRequestDto bannerRegisterRequestDto,
            @RequestPart(value = "bannerImg") MultipartFile bannerImg,
            @PathVariable Long bannerId
    ) {
        BannerRegisterResponseDto response = bannerService.bannerModify(bannerRegisterRequestDto, bannerImg, bannerId);
        return ApiResponse.createSuccessWithOk(response);
    }

    // 배너 삭제
    @DeleteMapping("/banner/{bannerId}")
    public ResponseEntity<ApiResponse<String>> bannerModify(
            @PathVariable Long bannerId
    ) {
        bannerService.bannerDelete(bannerId);
        return ApiResponse.createSuccessWithOk("배너 삭제 완료");
    }
}
