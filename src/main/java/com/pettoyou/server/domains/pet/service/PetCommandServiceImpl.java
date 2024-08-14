package com.pettoyou.server.domains.pet.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterAndModifyReqDto;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.util.S3Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PetCommandServiceImpl implements PetCommandService {
    private final S3Util s3Util;
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    @Override
    public PetRegisterRespDto petRegister(
            MultipartFile petProfileImg,
            PetRegisterAndModifyReqDto petRegisterDto,
            Long authMemberId
    ) {
        Member member = findMemberById(authMemberId);
        PhotoData photoData = processPhotoData(petProfileImg, null);

        Pet registeredPet = petRepository.save(Pet.of(petRegisterDto, photoData, member));

        return PetRegisterRespDto.from(registeredPet.getPetName());
    }

    @Override
    public void petModify(
            Long petId,
            MultipartFile petProfileImg,
            PetRegisterAndModifyReqDto petModifyDto,
            Long authMemberId
    ) {
        Pet pet = findPetById(petId);
        pet.validateOwnerAuthorization(authMemberId);

        PhotoData newPhotoData = processPhotoData(petProfileImg, pet.getProfilePhotoData());

        pet.modify(petModifyDto, newPhotoData);
    }

    @Override
    public void petDelete(
            Long petId,
            Long authMemberId
    ) {
        Pet pet = findPetById(petId);
        pet.validateOwnerAuthorization(authMemberId);

        petRepository.delete(pet);
    }

    private PhotoData processPhotoData(MultipartFile petProfileImg, PhotoData existingPhotoData) {
        if (petProfileImg != null && !petProfileImg.isEmpty()) {
            if (existingPhotoData != null) {
                s3Util.deleteFile(existingPhotoData.getBucket(), existingPhotoData.getObject());
            }
            return s3Util.uploadFile(petProfileImg);
        }
        return PhotoData.generateDefaultPetProfilePhotoData();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND)
        );
    }

    private Pet findPetById(Long petId) {
        return petRepository.findById(petId).orElseThrow(
                () -> new CustomException(CustomResponseStatus.PET_NOT_FOUND)
        );
    }
}
