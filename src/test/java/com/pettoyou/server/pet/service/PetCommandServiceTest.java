package com.pettoyou.server.pet.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.entity.enums.MemberStatus;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.pet.dto.request.PetMedicalInfoDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterAndModifyReqDto;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.entity.enums.*;
import com.pettoyou.server.domains.pet.service.PetCommandServiceImpl;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.util.S3Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetCommandServiceTest {
    @Mock
    private PetRepository petRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private S3Util s3Util;

    @InjectMocks
    private PetCommandServiceImpl petService;

    /***
     * 반려동물 등록 테스트
     */

    @Test
    void 프로필이미지와_반려동물의_정보를_입력한_경우_정상적으로_등록된다() {
        // given
        PetRegisterAndModifyReqDto petRegisterAndModifyReqDto = createPetFullyDto();
        Member member = createMember();
        Pet pet = createPet(petRegisterAndModifyReqDto, member);

        MockMultipartFile mockProfilePhoto = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test Image Content".getBytes()
        );

        when(memberRepository.findByMemberId(any(Long.class))).thenReturn(Optional.of(member));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // when
        PetRegisterRespDto result = petService.petRegister(mockProfilePhoto, petRegisterAndModifyReqDto, member.getMemberId());

        // then
        assertThat(result.petName()).isEqualTo(pet.getPetName());
    }

    @Test
    void 이미지가_있다면_uploadFile_메서드가_실행된다() {
        // given
        PetRegisterAndModifyReqDto petRegisterAndModifyReqDto = createPetFullyDto();
        Member member = createMember();
        Pet pet = createPet(petRegisterAndModifyReqDto, member);

        MockMultipartFile mockProfilePhoto = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test Image Content".getBytes()
        );

        when(memberRepository.findByMemberId(any(Long.class))).thenReturn(Optional.of(member));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // when
        petService.petRegister(mockProfilePhoto, petRegisterAndModifyReqDto, member.getMemberId());

        // then
        verify(s3Util, times(1)).uploadFile(mockProfilePhoto);
    }

    @Test
    void 프로필이미지_없이_반려동물의_정보를_입력한_경우_정상적으로_등록된다() {
        // given
        PetRegisterAndModifyReqDto petRegisterAndModifyReqDto = createPetFullyDto();
        Member member = createMember();
        Pet pet = createPet(petRegisterAndModifyReqDto, member);

        when(memberRepository.findByMemberId(any(Long.class))).thenReturn(Optional.of(member));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // when
        PetRegisterRespDto response = petService.petRegister(null, petRegisterAndModifyReqDto, member.getMemberId());

        // then
        assertThat(response.petName()).isEqualTo(pet.getPetName());
    }

    @Test
    void 이미지가_없다면_uploadFile_메서드가_실행되지_않는다() {
        // given
        PetRegisterAndModifyReqDto petRegisterAndModifyReqDto = createPetFullyDto();
        Member member = createMember();
        Pet pet = createPet(petRegisterAndModifyReqDto, member);

        when(memberRepository.findByMemberId(any(Long.class))).thenReturn(Optional.of(member));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // when
        petService.petRegister(null, petRegisterAndModifyReqDto, member.getMemberId());

        // then
        verify(s3Util, times(0)).uploadFile(null);
    }

    @Test
    void 잘못된_유저_ID_로_반려동물_등록_요청을하면_실패한다() {
        // given
        PetRegisterAndModifyReqDto petRegisterAndModifyReqDto = createPetFullyDto();
        Member member = createMember();
        long wrongMemberId = member.getMemberId() + 1;

        when(memberRepository.findByMemberId(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> {
                    petService.petRegister(null, petRegisterAndModifyReqDto, wrongMemberId);
                })
                .withMessage(CustomResponseStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 입양일을_입력하지_않은_경우_생년월일이_입양일이_된다() {
        // given
        PetRegisterAndModifyReqDto petNoAdoptionDateDto = createPetNoAdoptionDateDto();
        Member savedMember = createMember();

        // when
        LocalDate adoptionDate = createPet(petNoAdoptionDateDto, savedMember).getAdoptionDate();

        // then
        assertThat(adoptionDate).isEqualTo(LocalDate.of(2023, 7, 24));
    }

    /***
     * 반려동물 수정 테스트
     */

    @Test
    void 정상적인_반려동물_수정요청() {
        // given
        PetRegisterAndModifyReqDto modifyReqDto = createModifyReqDto();
        Member member = createMember();
        Pet pet = createPet(member);

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        // when
        petService.petModify(pet.getPetId(), null, modifyReqDto, member.getMemberId());

        // then
        assertThat(pet.getPetName()).isEqualTo(modifyReqDto.petName());
        assertThat(pet.getSpecies()).isEqualTo(modifyReqDto.species());
        assertThat(pet.getBirth()).isEqualTo(modifyReqDto.birth());
        assertThat(pet.getPetType()).isEqualTo(modifyReqDto.petType());
        assertThat(pet.getGender()).isEqualTo(modifyReqDto.gender());
        assertThat(pet.getCaution()).isEqualTo(modifyReqDto.caution());
        assertThat(pet.getAdoptionDate()).isEqualTo(modifyReqDto.adoptionDate());
        assertThat(pet.getPetMedicalInfo().getWeight()).isEqualTo(modifyReqDto.petMedicalInfoDto().weight());
    }

    @Test
    void 반려동물_id가_올바르지_않을때_예외발생() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);
        PetRegisterAndModifyReqDto modifyReqDto = createModifyReqDto();

        long wrongPetId = pet.getPetId() + 1;

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> petService.petModify(wrongPetId, null, modifyReqDto, member.getMemberId()))
                .withMessage(CustomResponseStatus.PET_NOT_FOUND.getMessage());
    }

    @Test
    void 수정_요청_유저_id와_반려동물_주인_id가_다를경우_예외발생() {
        // given
        PetRegisterAndModifyReqDto modifyReqDto = createModifyReqDto();
        Member member = createMember();
        Pet pet = createPet(member);

        long wrongMemberId = member.getMemberId() + 1;

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> petService.petModify(pet.getPetId(), null, modifyReqDto, wrongMemberId))
                .withMessage(CustomResponseStatus.MEMBER_NOT_MATCH.getMessage());
    }

    /***
     * 반려동물 삭제 테스트
     */

    @Test
    void 반려동물_정상_삭제() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        // when
        petService.petDelete(pet.getPetId(), member.getMemberId());

        // then
        verify(petRepository, times(1)).delete(pet);
    }

    @Test
    void 반려동물_id가_올바르지_않을때_예외가_발생한다() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);

        long wrongPetId = pet.getPetId() + 1;

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> petService.petDelete(wrongPetId, member.getMemberId()))
                .withMessage(CustomResponseStatus.PET_NOT_FOUND.getMessage());
    }

    @Test
    void 삭제_요청_유저_id와_반려동물_주인_id가_다를경우_예외발생() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);

        long wrongMemberId = member.getMemberId() + 1;

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> petService.petDelete(pet.getPetId(), wrongMemberId))
                .withMessage(CustomResponseStatus.MEMBER_NOT_MATCH.getMessage());
    }

    private PetRegisterAndModifyReqDto createPetFullyDto() {
        return PetRegisterAndModifyReqDto.builder()
                .petType(PetType.DOG)
                .petName("smile")
                .birth(LocalDate.of(2023, 7, 24))
                .adoptionDate(LocalDate.of(2023, 6, 24))
                .gender(Gender.MALE)
                .species(Species.MUNCHKIN)
                .caution("caution")
                .petMedicalInfoDto(
                        PetMedicalInfoDto.builder()
                                .weight(4.5)
                                .bmi(Bmi.NORMAL)
                                .registerNumber("1234")
                                .neuteringStatus(NeuteringStatus.NEUTERED)
                                .vaccinationStatus(VaccinationStatus.NOT_VACCINATED)
                                .allergy("알러지 없어요")
                                .currentFeedName("맛있는 사료")
                                .medicalHistory("수술한적 없음")
                                .build())
                .build();
    }

    private PetRegisterAndModifyReqDto createPetNoAdoptionDateDto() {
        return PetRegisterAndModifyReqDto.builder()
                .petType(PetType.DOG)
                .petName("smile")
                .birth(LocalDate.of(2023, 7, 24))
                .gender(Gender.MALE)
                .species(Species.MUNCHKIN)
                .caution("caution")
                .petMedicalInfoDto(
                        PetMedicalInfoDto.builder()
                                .weight(4.5)
                                .bmi(Bmi.NORMAL)
                                .registerNumber("1234")
                                .neuteringStatus(NeuteringStatus.NEUTERED)
                                .vaccinationStatus(VaccinationStatus.NOT_VACCINATED)
                                .allergy("알러지 없어요")
                                .currentFeedName("맛있는 사료")
                                .medicalHistory("수술한적 없음")
                                .build())
                .build();
    }

    private PetRegisterAndModifyReqDto createModifyReqDto() {
        return PetRegisterAndModifyReqDto.builder()
                .petType(PetType.DOG)
                .petName("modifyName")
                .birth(LocalDate.of(2023, 7, 24))
                .gender(Gender.MALE)
                .species(Species.MUNCHKIN)
                .caution("caution")
                .petMedicalInfoDto(
                        PetMedicalInfoDto.builder()
                                .weight(4.5)
                                .bmi(Bmi.NORMAL)
                                .registerNumber("1234")
                                .neuteringStatus(NeuteringStatus.NEUTERED)
                                .vaccinationStatus(VaccinationStatus.NOT_VACCINATED)
                                .allergy("알러지 없어요")
                                .currentFeedName("맛있는 사료")
                                .medicalHistory("수술한적 없음")
                                .build())
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .memberId(1L)
                .name("성춘")
                .nickName("choon")
                .phone("010-7592-0693")
                .email("test@naver.com")
                .provider(OAuthProvider.KAKAO)
                .providerId("3535")
                .memberStatus(MemberStatus.ACTIVATE)
                .build();
    }

    private PhotoData createPhotoData() {
        return PhotoData.of(
                "testBucket",
                "testObject",
                "testUrl"
        );
    }

    private Pet createPet(PetRegisterAndModifyReqDto petRegisterAndModifyReqDto, Member member) {
        return Pet.of(petRegisterAndModifyReqDto, createPhotoData(), member);
    }

    private Pet createPet(Member member) {
        return Pet.builder()
                .petId(1L)
                .petName("testPet")
                .member(member)
                .caution("caution")
                .birth(LocalDate.of(2023, 7, 27))
                .petType(PetType.DOG)
                .gender(Gender.MALE)
                .build();
    }

}