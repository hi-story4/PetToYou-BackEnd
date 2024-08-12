package com.pettoyou.server.pet.service.query;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.entity.enums.MemberStatus;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.entity.enums.Gender;
import com.pettoyou.server.domains.pet.entity.enums.PetType;
import com.pettoyou.server.domains.pet.entity.enums.Species;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.pet.service.query.PetQueryServiceImpl;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetQueryServiceTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetQueryServiceImpl petQueryService;

    /***
     * 반려동물 조회 테스트
     */

    @Test
    void 반려동물_정상_조회() {
        // given
        Member member = createMember();
        List<PetDetailInfoRespDto> petList = createPetDetailInfoDtoList();

        when(petRepository.findAllPetsByMemberId(any(Long.class))).thenReturn(petList);

        // when
        List<PetDetailInfoRespDto> result = petQueryService.queryPetList(member.getMemberId());

        // then
        assertThat(result.size()).isEqualTo(petList.size());
        for (int i = 0; i < petList.size(); i++) {
            assertThat(result.get(i).petId()).isEqualTo(petList.get(i).petId());
            assertThat(result.get(i).petName()).isEqualTo(petList.get(i).petName());
            assertThat(result.get(i).age()).isEqualTo(petList.get(i).age());
            assertThat(result.get(i).gender()).isIn("남아", "여아", "알수없음");
        }
    }

    @Test
    void 반려동물이_없는_경우_빈_리스트_반환() {
        // given
        List<PetDetailInfoRespDto> emptyList = createEmptyPetDetailInfoDtoList();

        when(petRepository.findAllPetsByMemberId(any(Long.class))).thenReturn(emptyList);

        // when
        List<PetDetailInfoRespDto> result = petQueryService.queryPetList(1L);

        // then
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void 반려동물_정상_상세_조회() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);
        PetDetailInfoRespDto petDetailInfoDto = createPetDetailInfoDto();

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        // when
        PetDetailInfoRespDto result = petQueryService.fetchPetDetailInfo(pet.getPetId(), member.getMemberId());

        // then
        assertThat(result.petId()).isEqualTo(petDetailInfoDto.petId());
    }

    @Test
    void 반려동물_상세조회시_반려동물의_id_값이_잘못된_경우_예외발생() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);
        long wrongPetId = pet.getPetId() + 1;

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> petQueryService.fetchPetDetailInfo(wrongPetId, member.getMemberId()))
                .withMessage(CustomResponseStatus.PET_NOT_FOUND.getMessage());
    }

    @Test
    void 반려동물_상세조회시_요청_유저의_id와_반려동물의_주인_id값이_다른_경우_예외발생() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);
        long wrongMemberId = member.getMemberId() + 1;

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        //then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> petQueryService.fetchPetDetailInfo(pet.getPetId(), wrongMemberId))
                .withMessage(CustomResponseStatus.MEMBER_NOT_MATCH.getMessage());
    }

    private List<PetDetailInfoRespDto> createPetDetailInfoDtoList() {
        List<PetDetailInfoRespDto> list = new ArrayList<>();

        for(long i=0; i<5; i++) {
            list.add(
                    PetDetailInfoRespDto.builder()
                            .petId(i)
                            .petName("pet"+i)
                            .gender("남아")
                            .species("시츄")
                            .age(15)
                            .build()
            );
        }

        return list;
    }

    private List<PetDetailInfoRespDto> createEmptyPetDetailInfoDtoList() {
        return new ArrayList<>();
    }

    private PetDetailInfoRespDto createPetDetailInfoDto() {
        return PetDetailInfoRespDto.builder()
                .petId(1L)
                .petName("testName")
                .species(Species.MUNCHKIN.getKoreanName())
                .petType(PetType.CAT)
                .build();
    }

    private Pet createPet(Member member) {
        return Pet.builder()
                .petId(1L)
                .petName("testPet")
                .member(member)
                .species(Species.MUNCHKIN)
                .birth(LocalDate.of(2023, 7, 27))
                .petType(PetType.CAT)
                .gender(Gender.MALE)
                .profilePhotoData(
                        PhotoData.generateDefaultPetProfilePhotoData()
                )
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .memberId(1L)
                .name("testName")
                .phone("010-1111-1111")
                .email("test@naver.com")
                .provider(OAuthProvider.KAKAO)
                .providerId("1234")
                .memberStatus(MemberStatus.ACTIVATE)
                .build();
    }
}