package com.pettoyou.server.domains.pet.service;

import com.pettoyou.server.domains.pet.dto.request.PetModifyReqDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterReqDto;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;

public interface PetCommandService {
    /***
     * 반려동물을 등록합니다.
     * @param petRegisterDto : 반려동물 등록시 필요한 반려동물의 정보입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     * @return : 등록한 반려동물의 이름이 반환됩니다.
     */
    PetRegisterRespDto registerPet(
            PetRegisterReqDto petRegisterDto,
            Long authMemberId
    );

    /***
     * 반려동물 정보를 수정합니다.
     * @param petId : 수정할 반려동물의 ID(PK) 입니다.
     * @param petRegisterDto : 수정할 반려동물의 정보입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     */
    void modifyPet(
            Long petId,
            PetModifyReqDto petRegisterDto,
            Long authMemberId
    );

    /***
     * 반려동물을 삭제합니다.
     * @param petId : 삭제할 반려동물의 ID(PK) 입니다.
     * @param authMemberId : 인가처리를 위한 요청을 보낸 클라이언트의 ID(PK) 입니다.
     */
    void deletePet(
            Long petId,
            Long authMemberId
    );
}
