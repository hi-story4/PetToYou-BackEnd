package com.pettoyou.server.domains.healthNote.controller;

import com.pettoyou.server.config.security.service.member.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;
import com.pettoyou.server.domains.healthNote.service.HealthNoteCommandService;
import com.pettoyou.server.domains.healthNote.service.query.HealthNoteQueryService;
import com.pettoyou.server.domains.healthNote.dto.request.HealthNoteRegistAndModifyReqDto;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteDetailInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/")
public class HealthNoteController {
    private final HealthNoteCommandService healthNoteCommandService;
    private final HealthNoteQueryService healthNoteQueryService;

    @PostMapping("healthNote")
    public ResponseEntity<ApiResponse<String>> registHealthNote(
            @RequestBody HealthNoteRegistAndModifyReqDto registReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        healthNoteCommandService.registHealthNote(registReqDto, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("건강수첩 등록완료");
    }

    @PutMapping("healthNote/{healthNoteId}")
    public ResponseEntity<ApiResponse<String>> modifyHealthNote(
            @PathVariable Long healthNoteId,
            @RequestBody HealthNoteRegistAndModifyReqDto modifyReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        healthNoteCommandService.modifyHealthNote(healthNoteId, modifyReqDto, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("건강수첩 수정완료");
    }

    @DeleteMapping("healthNote/{healthNoteId}")
    public ResponseEntity<ApiResponse<String>> deleteHealthNote(
            @PathVariable Long healthNoteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        healthNoteCommandService.deleteHealthNote(healthNoteId, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("건강수첩 삭제완료");
    }

    @GetMapping("healthNotes/{petId}")
    public ResponseEntity<ApiResponse<List<HealthNoteSimpleInfoDto>>> fetchHealthNoteByPetId(
            @PathVariable Long petId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<HealthNoteSimpleInfoDto> response = healthNoteQueryService.fetchHealthNotesByPetId(petId, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }

    @GetMapping("healthNote/{healthNoteId}")
    public ResponseEntity<ApiResponse<HealthNoteDetailInfoDto>> fetchHealthNoteDetailInfo(
            @PathVariable Long healthNoteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        HealthNoteDetailInfoDto response = healthNoteQueryService.fetchHealthNoteDetailInfo(healthNoteId, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }
}
