package com.pettoyou.server.domains.review.controller;

import com.pettoyou.server.config.security.service.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.review.service.ReviewService;
import com.pettoyou.server.domains.review.dto.ReviewReqDto;
import com.pettoyou.server.domains.review.dto.ReviewRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class ReviewController {

    ReviewService reviewService;

    @PostMapping("/store/{storeId}/review")
    public ResponseEntity<ApiResponse<String>> registerReview(
            @PathVariable Long storeId,
            @RequestParam Long petId,
            @RequestPart(required = false,value = "reviewImgs") List<MultipartFile> reviewImgs,
            @RequestPart(value = "reviewReqDto") ReviewReqDto reviewReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ) {
        Long userId = principalDetails.getUserId();
        log.info("User Id: " + userId);
        String reviewId = reviewService.registerReiview(storeId,petId, userId, reviewImgs, reviewReqDto);

        String path = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path + "/{reviewId}")
                .buildAndExpand(reviewId)  // reviewId 변수를 정확히 전달
                .toUri();

        return ApiResponse.createSuccessWithCreated("리뷰 등록 완료!" , location);
    }

    @GetMapping("/store/{storeId}/review")
    public ResponseEntity<ApiResponse<Page<ReviewRespDto>>> getReview(@PathVariable Long storeId,
                                                                      @PageableDefault(size = 10, sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<ReviewRespDto> reviewRespDto = reviewService.getReview(storeId, pageable);
        return ApiResponse.createSuccessWithOk(reviewRespDto);
    }


    @PreAuthorize("isAuthenticated() and (( #memberId == #principalDetails.userId) or hasRole('ADMIN'))")
    @DeleteMapping("/review/{reivewId}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable Long reivewId, @RequestParam Long memberId,
                                                            @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        reviewService.deleteReview(reivewId);
        return ApiResponse.createSuccessWithOk("리뷰 삭제 완료!");

    }

    //수정기능
    @PreAuthorize("isAuthenticated() and (( #memberId == #principalDetails.userId) or hasRole('ADMIN'))")
    @PutMapping("/review/{reivewId}")
    public ResponseEntity<ApiResponse<String>> putReview(@PathVariable Long reivewId,
                                                         @RequestPart(value="reviewReqDto") ReviewReqDto reviewReqDto,
                                                         @RequestPart(value = "reviewImgs") List<MultipartFile> reviewImgs,
                                                         @RequestParam Long memberId,
                                                         @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        reviewService.putReview(reivewId, reviewImgs, reviewReqDto);
        return ApiResponse.createSuccessWithOk("리뷰 수정 완료");

    }

    //상단고정 기능
    //병원관리자 본인 병원인지 로직 추가, PrincipalDetails 추가.
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PatchMapping("/review/{reivewId}/pinned")
    public ResponseEntity<ApiResponse<String>> patchReviewPinned(@PathVariable Long reivewId,
                                                               @RequestParam Integer pinned)
    {
        long result = reviewService.patchReviewPinned(reivewId, pinned);
        if(result<1) {throw new CustomException(CustomResponseStatus.INTERNAL_SERVER_ERROR);}
        return ApiResponse.createSuccessWithOk("상단고정 수정 완료");

    }
    //리뷰 신고기능은 컨트롤러 따로 만들면 좋겠는데 ?


}
