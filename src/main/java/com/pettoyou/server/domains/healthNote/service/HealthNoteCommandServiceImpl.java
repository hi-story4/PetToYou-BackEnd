package com.pettoyou.server.domains.healthNote.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import com.pettoyou.server.domains.healthNote.dto.request.HealthNoteRegistAndModifyReqDto;
import com.pettoyou.server.domains.healthNote.repository.HealthNoteRepository;
import com.pettoyou.server.domains.healthNote.validator.HealthNoteValidator;
import com.pettoyou.server.domains.hospital.validator.HospitalValidator;
import com.pettoyou.server.domains.pet.validator.PetValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthNoteCommandServiceImpl implements HealthNoteCommandService {
    private final HealthNoteRepository healthNoteRepository;

    private final HealthNoteValidator healthNoteValidator;
    private final PetValidator petValidator;
    private final HospitalValidator hospitalValidator;

    @Override
    @Transactional
    public void registHealthNote(
            HealthNoteRegistAndModifyReqDto registReqDto,
            Long authMemberId
    ) {
        hospitalValidator.checkHospitalExist(registReqDto.hospitalId());
        petValidator.verifyPetAuthorization(registReqDto.petId(), authMemberId);

        healthNoteRepository.save(HealthNote.of(registReqDto, authMemberId));
    }

    @Override
    @Transactional
    public void modifyHealthNote(
            Long healthNoteId,
            HealthNoteRegistAndModifyReqDto modifyReqDto,
            Long authMemberId
    ) {
        HealthNote findHealthNote = fetchHealthNoteById(healthNoteId);

        healthNoteValidator.verifyHealthNoteAuthorization(findHealthNote, authMemberId);
        hospitalValidator.checkHospitalExist(modifyReqDto.hospitalId());
        petValidator.verifyPetAuthorization(modifyReqDto.petId(), authMemberId);

        findHealthNote.modifyHealthNote(modifyReqDto);
    }

    @Override
    @Transactional
    public void deleteHealthNote(
            Long healthNoteId,
            Long authMemberId
    ) {
        HealthNote findHealthNote = fetchHealthNoteById(healthNoteId);

        healthNoteValidator.verifyHealthNoteAuthorization(findHealthNote, authMemberId);

        healthNoteRepository.delete(findHealthNote);
    }

    private HealthNote fetchHealthNoteById(
            Long healthNoteId
    ) {
        return healthNoteRepository.findById(healthNoteId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.HEALTH_NOTE_NOT_FOUND)
        );
    }
}
