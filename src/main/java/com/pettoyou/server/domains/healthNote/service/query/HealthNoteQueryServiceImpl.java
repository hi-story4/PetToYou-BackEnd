package com.pettoyou.server.domains.healthNote.service.query;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteDetailInfoDto;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;
import com.pettoyou.server.domains.healthNote.repository.HealthNoteRepository;
import com.pettoyou.server.domains.hospital.repository.hospital.HospitalRepository;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthNoteQueryServiceImpl implements HealthNoteQueryService {
    private final PetRepository petRepository;
    private final HealthNoteRepository healthNoteRepository;
    private final HospitalRepository hospitalRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<HealthNoteSimpleInfoDto> fetchHealthNotesByPetId(Long petId, Long authMemberId) {
        Pet findPet = findPetById(petId);

        findPet.validateOwnerAuthorization(authMemberId);

        return healthNoteRepository.findHealthNotesByPetId(petId);
    }

    @Override
    public HealthNoteDetailInfoDto fetchHealthNoteDetailInfo(Long healthNoteId, Long authMemberId) {
        HealthNote findHealthNote = findHealthNoteById(healthNoteId);
        Member member = findMemberById(findHealthNote.getMemberId());

        member.validateMemberAuthorization(authMemberId);

        String storeName = hospitalRepository.getHospitalNameByStoreId(findHealthNote.getStoreId());
        String petName = petRepository.getPetNameByPetId(findHealthNote.getPetId());

        return HealthNoteDetailInfoDto.of(findHealthNote, petName, storeName);
    }

    private HealthNote findHealthNoteById(Long healthNoteId) {
        return healthNoteRepository.findById(healthNoteId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.HEALTH_NOTE_NOT_FOUND)
        );
    }

    private Pet findPetById(Long petId) {
        return petRepository.findById(petId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.PET_NOT_FOUND)
        );
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND)
        );
    }
}
