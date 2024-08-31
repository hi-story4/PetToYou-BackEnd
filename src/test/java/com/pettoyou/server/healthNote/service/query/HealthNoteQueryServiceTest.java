package com.pettoyou.server.healthNote.service.query;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteDetailInfoDto;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;
import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import com.pettoyou.server.domains.healthNote.repository.HealthNoteRepository;
import com.pettoyou.server.domains.healthNote.service.query.HealthNoteQueryServiceImpl;
import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.hospital.repository.hospital.HospitalRepository;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.entity.enums.MemberStatus;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.entity.enums.Gender;
import com.pettoyou.server.domains.pet.entity.enums.PetType;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthNoteQueryServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private HealthNoteRepository healthNoteRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private HealthNoteQueryServiceImpl healthNoteQueryService;

    /***
     * 건강수첩 리스트 조회
     */

    @Test
    void 반려동물별_건강수첩_정상_조회() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);
        List<HealthNoteSimpleInfoDto> healthNoteSimpleDtoList = createHealthNoteSimpleDtoList();

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(pet));
        when(healthNoteRepository.findHealthNotesByPetId(any(Long.class))).thenReturn(healthNoteSimpleDtoList);

        // when
        List<HealthNoteSimpleInfoDto> testResult = healthNoteQueryService.fetchHealthNotesByPetId(pet.getPetId(), member.getMemberId());

        // then
        assertThat(testResult.size()).isEqualTo(healthNoteSimpleDtoList.size());
        int index=0;
        for (HealthNoteSimpleInfoDto result : testResult) {
            assertThat(result.hospitalName()).isEqualTo(healthNoteSimpleDtoList.get(index).hospitalName());
            assertThat(result.medicalRecord()).isEqualTo(healthNoteSimpleDtoList.get(index).medicalRecord());
            assertThat(result.caution()).isEqualTo(healthNoteSimpleDtoList.get(index).caution());
            assertThat(result.visitDate()).isEqualTo(healthNoteSimpleDtoList.get(index++).visitDate());
        }
    }

    @Test
    void 반려동물의_id가_잘못되었을때_예외발생() {
        // given
        when(petRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> healthNoteQueryService.fetchHealthNotesByPetId(1L, 1L))
                .withMessage(CustomResponseStatus.PET_NOT_FOUND.getMessage());
    }

    @Test
    void 유효하지않은_유저가_접근할때_예외발생() {
        // given
        Member member = createMember();
        Pet pet = createPet(member);
        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> healthNoteQueryService.fetchHealthNotesByPetId(pet.getPetId(), member.getMemberId() + 1))
                .withMessage(CustomResponseStatus.MEMBER_NOT_MATCH.getMessage());
    }

    /***
     * 건강수첩 상세 조회
     */

    @Test
    void 건강수첩_정상_상세_조회() {
        // given
        HealthNote healthNote = createHealthNote();
        Member member = createMember();
        Hospital hospital = createHospital();
        Pet pet = createPet(member);

        when(healthNoteRepository.findById(any(Long.class))).thenReturn(Optional.of(healthNote));
        when(hospitalRepository.getHospitalNameNameByStoreId(any(Long.class))).thenReturn(hospital.getStoreName());
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(petRepository.getPetNameByPetId(any(Long.class))).thenReturn(pet.getPetName());

        // when
        HealthNoteDetailInfoDto result = healthNoteQueryService.fetchHealthNoteDetailInfo(hospital.getStoreId(), member.getMemberId());

        // then
        assertThat(result.hospitalName()).isEqualTo(hospital.getStoreName());
        assertThat(result.petName()).isEqualTo(pet.getPetName());
        assertThat(result.visitDate()).isEqualTo(healthNote.getVisitDate());
        assertThat(result.caution()).isEqualTo(healthNote.getCaution());
        assertThat(result.vetName()).isEqualTo(healthNote.getVetName());
        assertThat(result.medicalRecord()).isEqualTo(healthNote.getMedicalRecord());
    }

    @Test
    void 유효하지않은_건강수첩_id로_상세조회시_예외발생() {
        // given
        when(healthNoteRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> healthNoteQueryService.fetchHealthNoteDetailInfo(1L, 1L))
                .withMessage(CustomResponseStatus.HEALTH_NOTE_NOT_FOUND.getMessage());
    }

    @Test
    void 다른_유저의_건강수첩에_접근한_경우_예외발생() {
        // given
        HealthNote healthNote = createHealthNote();
        Member member = createMember();

        when(healthNoteRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(healthNote));
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> healthNoteQueryService.fetchHealthNoteDetailInfo(healthNote.getHealthNoteId(), healthNote.getMemberId() + 1))
                .withMessage(CustomResponseStatus.MEMBER_NOT_MATCH.getMessage());
    }

    private List<HealthNoteSimpleInfoDto> createHealthNoteSimpleDtoList() {
        List<HealthNoteSimpleInfoDto> list = new ArrayList<>();

        for(int i=1; i<=5; i++) {
            list.add(
                    HealthNoteSimpleInfoDto.builder()
                            .hospitalName("hospital"+i)
                            .medicalRecord("mr"+i)
                            .visitDate(LocalDate.of(2024, 7, i))
                            .caution("caution"+i)
                            .build()
            );
        }

        return list;
    }

    private HealthNote createHealthNote() {
        return HealthNote.builder()
                .healthNoteId(1L)
                .storeId(1L)
                .petId(1L)
                .memberId(1L)
                .visitDate(LocalDate.of(2023, 7, 24))
                .caution("first Caution")
                .medicalRecord("first mr")
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

    private Hospital createHospital() {
        return Hospital.builder()
                .storeId(1L)
                .storeName("testHospital")
                .build();
    }

    private Pet createPet(Member member) {
        return Pet.builder()
                .petId(1L)
                .petName("testPet")
                .member(member)
                .birth(LocalDate.of(2023, 7, 27))
                .petType(PetType.DOG)
                .gender(Gender.MALE)
                .build();
    }
}