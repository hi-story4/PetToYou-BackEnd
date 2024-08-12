package com.pettoyou.server.domains.healthNote.repository.custom;

import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;

import java.util.List;

public interface HealthNoteCustomRepository {

    List<HealthNoteSimpleInfoDto> findHealthNotesByPetId(Long petId);
}
