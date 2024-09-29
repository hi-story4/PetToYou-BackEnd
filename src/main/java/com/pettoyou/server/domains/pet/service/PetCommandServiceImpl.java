package com.pettoyou.server.domains.pet.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.pet.dto.request.PetModifyReqDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterReqDto;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PetCommandServiceImpl implements PetCommandService {
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    @Override
    public PetRegisterRespDto registerPet(PetRegisterReqDto petRegisterDto, Long authMemberId) {
        Member member = findMemberById(authMemberId);

        // Todo : 성운이가 프로필 업로드를 안할 경우 어떤 식으로 줄지에 맞춰서 코드 수정이 필요함
        PhotoData petProfilePhotoData = PhotoData.generateDefaultPetProfilePhotoData();
        if(petRegisterDto.petProfilePhotoDto() != null) {
            petProfilePhotoData = PhotoData.of(
                    petRegisterDto.petProfilePhotoDto().bucket(),
                    petRegisterDto.petProfilePhotoDto().object(),
                    petRegisterDto.petProfilePhotoDto().url()
            );
        }

        Pet registeredPet = petRepository.save(Pet.of(petRegisterDto, petProfilePhotoData, member));

        return PetRegisterRespDto.from(registeredPet.getPetName());
    }

    @Override
    public void modifyPet(
            Long petId,
            PetModifyReqDto petModifyDto,
            Long authMemberId
    ) {
        Pet pet = findPetById(petId);
        pet.validateOwnerAuthorization(authMemberId);

        PhotoData curPhotoData = pet.getProfilePhotoData();
        if (petModifyDto.petProfilePhotoDto()!= null) {
            curPhotoData = PhotoData.of(
                    petModifyDto.petProfilePhotoDto().bucket(),
                    petModifyDto.petProfilePhotoDto().object(),
                    petModifyDto.petProfilePhotoDto().url()
            );
        }

        pet.modifyPetInfo(petModifyDto, curPhotoData);
    }

    @Override
    public void deletePet(
            Long petId,
            Long authMemberId
    ) {
        Pet pet = findPetById(petId);
        pet.validateOwnerAuthorization(authMemberId);

        petRepository.delete(pet);
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
