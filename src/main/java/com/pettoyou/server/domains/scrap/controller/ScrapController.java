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
    public ResponseEntity<ApiResponse<ScrapRegistRespDto>> scrapRegister(
            @RequestBody ScrapRegistReqDto scrapRegistReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ScrapRegistRespDto response = scrapService.scrapRegist(scrapRegistReqDto.storeId(), principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }

    @DeleteMapping("/scrap/{scrapId}")
    public ResponseEntity<ApiResponse<String>> scrapRegister(
            @PathVariable Long scrapId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        scrapService.scrapCancel(scrapId, principalDetails.getUserId());
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
