package com.pettoyou.server.domains.healthNote.service.query;

import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteDetailInfoDto;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;

import java.util.List;

public interface HealthNoteQueryService {

    List<HealthNoteSimpleInfoDto> fetchHealthNotesByPetId(
            Long petId,
            Long authMemberId
    );

    HealthNoteDetailInfoDto fetchHealthNoteDetailInfo(
            Long healthNoteId,
            Long authMemberId
    );
}
