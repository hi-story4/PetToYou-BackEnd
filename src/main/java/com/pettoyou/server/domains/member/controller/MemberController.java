package com.pettoyou.server.domains.member.controller;

import com.pettoyou.server.config.security.service.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.member.dto.request.MemberInfoModifyReqDto;
import com.pettoyou.server.domains.member.dto.response.MemberInfoQueryDto;
import com.pettoyou.server.domains.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /***
     * 내 정보 수정하기
     */

    @PutMapping("/myPage")
    public ResponseEntity<ApiResponse<String>> modifyMemberInfo(
            @RequestBody MemberInfoModifyReqDto modifyReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        memberService.modifyMemberInfo(
                modifyReqDto,
                principalDetails.getUserId()
        );

        return ApiResponse.createSuccessWithOk("회원 정보 수정 완료");
    }


    /***
     * 내 정보 조회하기
     */

    @GetMapping("/myPage")
    public ResponseEntity<ApiResponse<MemberInfoQueryDto>> queryMemberInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        MemberInfoQueryDto response = memberService.queryMemberInfo(principalDetails.getUserId());

        return ApiResponse.createSuccessWithOk(response);
    }
}
