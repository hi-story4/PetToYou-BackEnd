package com.pettoyou.server.domains.review.service;

import com.pettoyou.server.config.security.service.PrincipalDetails;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.review.dto.ReviewReqDto;
import com.pettoyou.server.domains.review.dto.ReviewRespDto;
import com.pettoyou.server.domains.review.entity.Review;
import com.pettoyou.server.domains.review.repository.ReviewRepository;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.repository.StoreRepository;
import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final PetRepository petRepository;


    public String registerReiview(Long storeId, Long petId,  Long userId, List<MultipartFile> reviewImgs, ReviewReqDto reviewReqDto){
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(CustomResponseStatus.STORE_NOT_FOUND));
        Pet pet  = petRepository.findById(petId).orElseThrow(() -> new CustomException(CustomResponseStatus.PET_NOT_FOUND));
        String storeType = store.getDtype();
        //병원 or 미용실
        Review reviewEntity = ReviewReqDto.toEntity(store, pet, userId, reviewReqDto, storeType);
        Review result = reviewRepository.save(reviewEntity);
        log.info("reviwId : " + result.getReviewId());
        return result.getReviewId().toString();
    }
    public Page<ReviewRespDto> getReview(Long storeId, Pageable pageable){
       return reviewRepository.findReviewsFetchJoinPetsByStoreId(storeId, pageable);
    }
    public void deleteReview(Long reivewId,PrincipalDetails principalDetails){
        Review review = reviewRepository.findById(reivewId).orElseThrow(() -> new CustomException(CustomResponseStatus.REVIEW_NOT_FOUND));

        if(principalDetails.getAuthorities() != null && (review.getMemberId().equals(principalDetails.getUserId()) ||  principalDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))))
        {
            reviewRepository.delete(review);
        }


    }

    public long patchReviewPinned(Long reivewId, Integer pinned){
        return reviewRepository.updatePinned(reivewId, pinned);
    }
    public void putReview(Long reivewId, PrincipalDetails principalDetails, List<MultipartFile> reviewImgs, ReviewReqDto reviewReqDto) {
        //펫 수정은 불가능
        Review review = reviewRepository.findById(reivewId).orElseThrow(() -> new CustomException(CustomResponseStatus.REVIEW_NOT_FOUND));

        log.info("userID : "+ principalDetails.getUserId());
        //사진 수정 제외

        if(principalDetails.getAuthorities() != null && (review.getMemberId().equals(principalDetails.getUserId()) ||  principalDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))))
        {
            review.modify(reviewReqDto);
        }
        else throw new CustomException(CustomResponseStatus.ACCESS_DENIED);
    }
}
