package com.pettoyou.server.domains.pet.controller;

import com.pettoyou.server.config.security.service.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterAndModifyReqDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterReqDto;
import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;
import com.pettoyou.server.domains.pet.service.PetCommandService;
import com.pettoyou.server.domains.pet.service.query.PetQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class PetController {
    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;

    @PostMapping("/pet")
    public ResponseEntity<ApiResponse<PetRegisterRespDto>> petRegister(
            @RequestPart(required = false, value = "petProfileImg") MultipartFile petProfileImg,
            @RequestPart(value = "petRegisterDto") @Valid PetRegisterAndModifyReqDto petRegisterDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PetRegisterRespDto response = petCommandService.petRegister(petProfileImg, petRegisterDto, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }

    @PostMapping("/petv2")
    public ResponseEntity<ApiResponse<PetRegisterRespDto>> petRegisterV2(
            @RequestPart(value = "petRegisterDto") @Valid PetRegisterReqDto petRegisterDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PetRegisterRespDto response = petCommandService.petRegisterV2(petRegisterDto, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }

    @PutMapping("/pet/{id}")
    public ResponseEntity<ApiResponse<String>> petModify(
            @PathVariable Long id,
            @RequestPart(required = false, value = "petProfileImg") MultipartFile petProfileImg,
            @RequestPart(value = "petModifyDto") @Valid PetRegisterAndModifyReqDto petRegisterDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        petCommandService.petModify(id, petProfileImg, petRegisterDto, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("반려동물 정보 수정 완료");
    }

    @DeleteMapping("/pet/{id}")
    public ResponseEntity<ApiResponse<String>> petDelete(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        petCommandService.petDelete(id, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk("반려동물 삭제 완료");
    }

    @GetMapping("/pets")
    public ResponseEntity<ApiResponse<List<PetDetailInfoRespDto>>> petsQuery(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<PetDetailInfoRespDto> response = petQueryService.queryPetList(principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<ApiResponse<PetDetailInfoRespDto>> fetchPetDetailInfo(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PetDetailInfoRespDto response = petQueryService.fetchPetDetailInfo(id, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(response);
    }
}
