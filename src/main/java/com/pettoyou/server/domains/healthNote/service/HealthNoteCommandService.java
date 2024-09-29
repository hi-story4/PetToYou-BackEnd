package com.pettoyou.server.domains.healthNote.service;

import com.pettoyou.server.domains.healthNote.dto.request.HealthNoteRegistAndModifyReqDto;

public interface HealthNoteCommandService {

    /***
     * 건강수첩을 등록합니다.
     * @param healthNoteRegistAndModifyReqDto : 건강수첩 등록시 필요한 정보입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     */
    void registHealthNote(
            HealthNoteRegistAndModifyReqDto healthNoteRegistAndModifyReqDto,
            Long authMemberId
    );

    /***
     * 건강수첩을 수정합니다.
     * @param healthNoteId : 수정할 건강수첩의 ID(PK) 입니다.
     * @param healthNoteRegistAndModifyReqDto : 건강수첩 수정시 필요한 정보입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     */
    void modifyHealthNote(
            Long healthNoteId,
            HealthNoteRegistAndModifyReqDto healthNoteRegistAndModifyReqDto,
            Long authMemberId
    );

    /***
     * 건강수첩을 삭제합니다.
     * @param healthNoteId : 삭제할 건강수첩의 ID(PK) 입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     */
    void deleteHealthNote(
            Long healthNoteId,
            Long authMemberId
    );
}
