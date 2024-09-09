package com.pettoyou.server.domains.hospital.controller;

import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.domains.hospital.dto.request.HospitalDto;
import com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin.HospitalAdminSignInReqDto;
import com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin.HospitalAdminSignUpReqDto;
import com.pettoyou.server.domains.hospital.service.HospitalService;

import com.pettoyou.server.domains.hospital.service.auth.HospitalAdminService;
import com.pettoyou.server.domains.member.dto.response.LoginAndReissueRespDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Hospital", description = "Hospital 관련 API 입니다.")
@RequestMapping("/api/v1/hospital/admin")
@Slf4j
public class HospitalControllerAdmin {

    private final HospitalService hospitalService;
    private final HospitalAdminService hospitalAdminService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> registerHospital(
            @RequestPart(required = false, value = "hospitalImg") List<MultipartFile> hospitalImg,
            @RequestPart(required = false, value = "storeInfoImg") MultipartFile storeInfoImg,
            @RequestPart(required = false, value = "thumbnailImg") MultipartFile thumbnailImg,
            @RequestPart(value = "hospitalDto") @Valid HospitalDto hospitalDto
    ) {

        String hospitalId = hospitalService.registerHospital(hospitalImg, storeInfoImg, thumbnailImg, hospitalDto);

        // 현재 요청 URI에서 path 부분만 가져오기
        String currentPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        // "/admin" 부분 제거하기
        String newPath = currentPath.replace("/admin", "");
        // 새로운 URI 생성
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(newPath + "/{id}")
                .buildAndExpand(hospitalId)
                .toUri();

        return ApiResponse.createSuccessWithCreated("병원 등록 완료!", location);
    }

    /***
     * 병원 관리자 회원가입
     */
    @PostMapping("sign-up")
    public ResponseEntity<ApiResponse<String>> signUp(
            @RequestBody HospitalAdminSignUpReqDto signUpReqDto
    ) {
        hospitalAdminService.singUp(signUpReqDto);
        return ApiResponse.createSuccessWithOk("회원가입이 완료되었습니다.");
    }

    @PostMapping("sign-in")
    public ResponseEntity<ApiResponse<LoginAndReissueRespDto>> singIn(
            @RequestBody HospitalAdminSignInReqDto signInReqDto
    ) {
        AuthTokens authTokens = hospitalAdminService.signIn(signInReqDto);

        return ApiResponse.createSuccessWithOk(LoginAndReissueRespDto.from(authTokens));
    }
}
