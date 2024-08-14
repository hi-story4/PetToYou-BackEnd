package com.pettoyou.server.domains.review.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    StoreRepository storeRepository;
    ReviewRepository reviewRepository;
    PetRepository petRepository;


    public String registerReiview(Long storeId, Long petId,  Long userId, List<MultipartFile> reviewImgs, ReviewReqDto reviewReqDto){
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(CustomResponseStatus.STORE_NOT_FOUND));
        Pet pet  = petRepository.findById(petId).orElseThrow(() -> new CustomException(CustomResponseStatus.PET_NOT_FOUND));
        String storeType = store.getDtype();
        //병원 or 미용실
        Review reviewEntity = ReviewReqDto.toEntity(store, pet, userId, reviewReqDto, storeType);
        reviewRepository.save(reviewEntity);
        return reviewEntity.getReviewId().toString();
    }
    public Page<ReviewRespDto> getReview(Long storeId, Pageable pageable){
        Page<Tuple> reviewAndPet = reviewRepository.findReviewsFetchJoinPetsByStoreId(storeId, pageable);
         List<ReviewRespDto> result = reviewAndPet.stream()
                 .map(ReviewRespDto::toDto).toList();

         return new PageImpl<>(result, reviewAndPet.getPageable(), reviewAndPet.getTotalElements());
    }
    public void deleteReview(Long reivewId){
        reviewRepository.deleteById(reivewId);
    }

    public long patchReviewPinned(Long reivewId, Integer pinned){
        return reviewRepository.updatePinned(reivewId, pinned);
    }
    public void putReview(Long reivewId, List<MultipartFile> reviewImgs, ReviewReqDto reviewReqDto) {
        //Pet pet  = petRepository.findById(petId).orElseThrow(() -> new CustomException(CustomResponseStatus.PET_NOT_FOUND));
        //펫 수정은 추후 고려..할까?
        Review review = reviewRepository.findById(reivewId).orElseThrow(() -> new CustomException(CustomResponseStatus.REVIEW_NOT_FOUND));
        review.modify(reviewReqDto);
    }
}
