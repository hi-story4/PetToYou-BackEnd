package com.pettoyou.server.domains.scrap.controller;

import com.pettoyou.server.config.security.service.member.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.scrap.dto.request.ScrapRegistReqDto;
import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;
import com.pettoyou.server.domains.scrap.dto.response.ScrapRegistRespDto;
import com.pettoyou.server.domains.scrap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class ScrapController {
    private final ScrapService scrapService;

    @PostMapping("/scrap")
    public ResponseEntity<ApiResponse<String>> registScrap(
            @RequestBody ScrapRegistReqDto scrapRegistReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        scrapService.registScrap(scrapRegistReqDto.storeId(), principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("찜 등록 완료");
    }

    @DeleteMapping("/scrap/{scrapId}")
    public ResponseEntity<ApiResponse<String>> cancelScrap(
            @PathVariable Long scrapId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        scrapService.cancelScrap(scrapId, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("찜 해제 완료");
    }

    @GetMapping("/scraps")
    public ResponseEntity<ApiResponse<List<ScrapQueryRespDto>>> fetchScrapList (
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<ScrapQueryRespDto> response = scrapService.fetchScrapStore(principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }
}
