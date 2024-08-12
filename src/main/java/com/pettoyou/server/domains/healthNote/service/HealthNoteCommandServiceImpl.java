package com.pettoyou.server.domains.healthNote.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import com.pettoyou.server.domains.healthNote.dto.request.HealthNoteRegistAndModifyReqDto;
import com.pettoyou.server.domains.healthNote.repository.HealthNoteRepository;
import com.pettoyou.server.domains.hospital.repository.HospitalRepository;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthNoteCommandServiceImpl implements HealthNoteCommandService {
    private final HealthNoteRepository healthNoteRepository;
    private final StoreRepository storeRepository;
    private final PetRepository petRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional
    public void registHealthNote(HealthNoteRegistAndModifyReqDto registReqDto, Long authMemberId) {
        checkValidHospital(registReqDto.hospitalId());
        checkValidPet(registReqDto.petId(), authMemberId);

        healthNoteRepository.save(HealthNote.of(registReqDto, authMemberId));
    }

    @Override
    @Transactional
    public void modifyHealthNote(Long healthNoteId, HealthNoteRegistAndModifyReqDto modifyReqDto, Long authMemberId) {
        HealthNote findHealthNote = fetchHealthNoteById(healthNoteId);

        findHealthNote.validateMemberAuthorization(authMemberId);
        checkValidHospital(modifyReqDto.hospitalId());
        checkValidPet(modifyReqDto.petId(), authMemberId);

        findHealthNote.modifyHealthNote(modifyReqDto);
    }

    @Override
    public void deleteHealthNote(Long healthNoteId, Long authMemberId) {
        HealthNote findHealthNote = fetchHealthNoteById(healthNoteId);
        findHealthNote.validateMemberAuthorization(authMemberId);

        healthNoteRepository.delete(findHealthNote);
    }

    private HealthNote fetchHealthNoteById(Long healthNoteId) {
        return healthNoteRepository.findById(healthNoteId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.HEALTH_NOTE_NOT_FOUND)
        );
    }

    private void checkValidHospital(Long hospitalId) {
        hospitalRepository.findById(hospitalId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.HOSPITAL_NOT_FOUND)
        );
    }

    private void checkValidPet(Long petId, Long authMemberId) {
        petRepository.findPetUsingPetIdAndMemberId(petId, authMemberId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.PET_NOT_FOUND)
        );
    }
}
