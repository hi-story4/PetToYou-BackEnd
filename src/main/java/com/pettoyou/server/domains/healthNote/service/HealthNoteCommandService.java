package com.pettoyou.server.domains.healthNote.service;

import com.pettoyou.server.domains.healthNote.dto.request.HealthNoteRegistAndModifyReqDto;

public interface HealthNoteCommandService {

    void registHealthNote(
            HealthNoteRegistAndModifyReqDto healthNoteRegistAndModifyReqDto,
            Long authMemberId
    );

    void modifyHealthNote(
            Long healthNoteId,
            HealthNoteRegistAndModifyReqDto healthNoteRegistAndModifyReqDto,
            Long authMemberId
    );

    void deleteHealthNote(
            Long healthNoteId,
            Long authMemberId
    );
}
