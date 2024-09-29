package com.pettoyou.server.domains.healthNote.service.query;

import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteDetailInfoDto;
import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;

import java.util.List;

public interface HealthNoteQueryService {

    /***
     * 반려동물별 건강수첩 간단정보를 조회합니다.
     * @param petId : 건강수첩을 조회할 반려동물 ID(PK) 입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     * @return : 조회된 건강수첩의 간단정보를 반환합니다.
     */
    List<HealthNoteSimpleInfoDto> fetchHealthNotesByPetId(
            Long petId,
            Long authMemberId
    );

    /***
     * 건강수첩을 상세조회합니다.
     * @param healthNoteId : 조회할 건강수첩의 ID(PK) 입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     * @return : 해당 건강수첩의 상세정보를 반환합니다.
     */
    HealthNoteDetailInfoDto fetchHealthNoteDetailInfo(
            Long healthNoteId,
            Long authMemberId
    );
}
