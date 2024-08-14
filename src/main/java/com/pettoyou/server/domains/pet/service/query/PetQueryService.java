package com.pettoyou.server.domains.pet.service.query;

import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;

import java.util.List;

public interface PetQueryService {
    List<PetDetailInfoRespDto> queryPetList(
            Long userId
    );

    PetDetailInfoRespDto fetchPetDetailInfo(
            Long petId,
            Long loginMemberId
    );
}
