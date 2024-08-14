package com.pettoyou.server.scrap.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.entity.Hospital;
import com.pettoyou.server.domains.hospital.repository.HospitalRepository;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.entity.enums.MemberStatus;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;
import com.pettoyou.server.domains.scrap.dto.response.ScrapRegistRespDto;
import com.pettoyou.server.domains.scrap.entity.Scrap;
import com.pettoyou.server.domains.scrap.repository.ScrapRepository;
import com.pettoyou.server.domains.scrap.service.ScrapServiceImpl;
import com.pettoyou.server.domains.store.entity.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScrapServiceTest {

    @Mock
    private ScrapRepository scrapRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private ScrapServiceImpl scrapService;

    /***
     * 스크랩 등록
     */

    @Test
    void 스크랩_정상_등록() {
        // given
        Member member = createMember();
        Hospital hospital = createHospital();
        Scrap scrap = createScrap(member, hospital);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(hospitalRepository.findById(any(Long.class))).thenReturn(Optional.of(hospital));
        when(scrapRepository.save(any(Scrap.class))).thenReturn(scrap);

        // when
        ScrapRegistRespDto result = scrapService.scrapRegist(hospital.getStoreId(), member.getMemberId());

        // then
        assertThat(result.storeName()).isEqualTo(hospital.getStoreName());
    }

    @Test
    void 존재하지_않는_유저의_경우_예외_발생() {
        // given
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> scrapService.scrapRegist(1L, 1L))
                .withMessage(CustomResponseStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 존재하지_않는_병원의_경우_예외_발생() {
        /// given
        Member member = createMember();

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(hospitalRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> scrapService.scrapRegist(1L, 1L))
                .withMessage(CustomResponseStatus.HOSPITAL_NOT_FOUND.getMessage());
    }

    /***
     * 스크랩 해제
     */

    @Test
    void 스크랩_정상_해제() {
        // given
        Member member = createMember();
        Hospital hospital = createHospital();
        Scrap scrap = createScrap(member, hospital);

        when(scrapRepository.findById(any(Long.class))).thenReturn(Optional.of(scrap));

        // when
        scrapService.scrapCancel(scrap.getScrapId(), member.getMemberId());

        // then
        verify(scrapRepository, times(1)).delete(scrap);
    }

    @Test
    void 존재하지않는_스크랩_해제_요청시_예외발생() {
        // given
        when(scrapRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> scrapService.scrapCancel(1L, 1L))
                .withMessage(CustomResponseStatus.SCRAP_NOT_FOUND.getMessage());
    }

    @Test
    void 다른_유저의_스크랩을_해제하려고_하는_경우_예외발생() {
        // given
        Member member = createMember();
        Hospital hospital = createHospital();
        Scrap scrap = createScrap(member, hospital);
        long wrongMemberId = member.getMemberId() + 1;

        when(scrapRepository.findById(any(Long.class))).thenReturn(Optional.of(scrap));

        // then
        assertThatExceptionOfType(CustomException.class)
                // when
                .isThrownBy(() -> scrapService.scrapCancel(scrap.getScrapId(), wrongMemberId))
                .withMessage(CustomResponseStatus.MEMBER_NOT_MATCH.getMessage());
    }

    /***
     * 스크랩 조회
     */

    @Test
    void 스크랩_목록_정상_조회() {
        // given
        List<ScrapQueryRespDto> scrapList = createScrapList();

        when(scrapRepository.findScrapListByMemberId(any(Long.class))).thenReturn(scrapList);

        // when
        List<ScrapQueryRespDto> result = scrapService.fetchScrapStore(1L);

        // then
        assertThat(result.size()).isEqualTo(scrapList.size());
        for (int i = 0; i < scrapList.size(); i++) {
            assertThat(result.get(i).scrapId()).isEqualTo(scrapList.get(i).scrapId());
            assertThat(result.get(i).address()).isEqualTo(scrapList.get(i).address());
            assertThat(result.get(i).thumbnailUrl()).isEqualTo(scrapList.get(i).thumbnailUrl());
            assertThat(result.get(i).storeName()).isEqualTo(scrapList.get(i).storeName());
        }
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

    private Hospital createHospital() {
        return Hospital.builder()
                .storeId(1L)
                .storeName("testHospital")
                .build();
    }

    private List<ScrapQueryRespDto> createScrapList() {
        List<ScrapQueryRespDto> list = new ArrayList<>();

        for (long i = 0; i < 5; i++) {
            list.add(new ScrapQueryRespDto(
                    i,
                    "test" + i + ".com",
                    "hospital" + i,
                    Address.builder()
                            .sido("sido" + i)
                            .sigungu("sigungu" + i)
                            .doro("doro" + i)
                            .build()
            ));
        }

        return list;
    }

    private Scrap createScrap(Member member, Hospital hospital) {
        return Scrap.builder()
                .scrapId(1L)
                .member(member)
                .store(hospital)
                .build();
    }
}