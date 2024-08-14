package com.pettoyou.server.domains.review.service;

import com.pettoyou.server.domains.review.dto.ReviewReqDto;
import com.pettoyou.server.domains.review.dto.ReviewRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {


        String registerReiview(Long storeId, Long petId, Long userId, List<MultipartFile> reviewImgs, ReviewReqDto reviewReqDto);

        Page<ReviewRespDto> getReview(Long storeId, Pageable pageable);

        void deleteReview(Long reivewId);

        void putReview(Long reivewId,List<MultipartFile> reviewImgs, ReviewReqDto reviewReqDto);

        long patchReviewPinned(Long reivewId, Integer pinned);

}
