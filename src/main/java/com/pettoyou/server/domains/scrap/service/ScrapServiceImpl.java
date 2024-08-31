package com.pettoyou.server.domains.scrap.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.hospital.repository.hospital.HospitalRepository;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;
import com.pettoyou.server.domains.scrap.dto.response.ScrapRegistRespDto;
import com.pettoyou.server.domains.scrap.entity.Scrap;
import com.pettoyou.server.domains.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapServiceImpl implements ScrapService {
    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    public ScrapRegistRespDto scrapRegist(Long storeId, Long authMemberId) {
        Member findMember = findMemberById(authMemberId);
        Hospital findHospital = findHospitalById(storeId);

        Scrap saveScrap = scrapRepository.save(Scrap.of(findMember, findHospital));

        return new ScrapRegistRespDto(saveScrap.getStore().getStoreName());
    }

    @Override
    public void scrapCancel(Long scrapId, Long authMemberId) {
        Scrap findScrap = findScrapById(scrapId);
        findScrap.validateOwnerAuthorization(authMemberId);

        scrapRepository.delete(findScrap);
    }

    @Override
    public List<ScrapQueryRespDto> fetchScrapStore(Long memberId) {
        return scrapRepository.findScrapListByMemberId(memberId);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND)
        );
    }

    private Hospital findHospitalById(Long hospitalId) {
        return hospitalRepository.findById(hospitalId).orElseThrow(
                () -> new CustomException(CustomResponseStatus.HOSPITAL_NOT_FOUND)
        );
    }

    private Scrap findScrapById(Long scrapId) {
        return scrapRepository.findById(scrapId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.SCRAP_NOT_FOUND)
        );
    }
}
