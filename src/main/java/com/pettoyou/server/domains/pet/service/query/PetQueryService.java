package com.pettoyou.server.domains.pet.service.query;

import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;

import java.util.List;

public interface PetQueryService {
    /***
     * 클라이언트의 반려동물 목록을 반환합니다.
     * @param userId : 로그인한 유저의 ID(PK) 입니다.
     * @return : 클라이언트의 반려동물 목록을 반환합니다.
     */
    List<PetDetailInfoRespDto> fetchClientPets(
            Long userId
    );

    /***
     * 반려동물의 상세정보를 반환합니다.
     * @param petId : 조회하려는 반려동물의 ID(PK) 입니다.
     * @param loginMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     * @return : 반려동물의 상세정보를 반환합니다.
     */
    PetDetailInfoRespDto fetchPetDetailInfo(
            Long petId,
            Long loginMemberId
    );
}
