package com.pettoyou.server.domains.pet.service;

import com.pettoyou.server.domains.pet.dto.request.PetModifyReqDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterAndModifyReqDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterReqDto;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;
import org.springframework.web.multipart.MultipartFile;

public interface PetCommandService {
    PetRegisterRespDto petRegister(
            MultipartFile petProfileImgs,
            PetRegisterAndModifyReqDto petRegisterDto,
            Long authMemberId
    );

    PetRegisterRespDto petRegisterV2(
            PetRegisterReqDto petRegisterDto,
            Long authMemberId
    );

    void petModify(
            Long petId,
            MultipartFile petProfileImg,
            PetRegisterAndModifyReqDto petRegisterDto,
            Long authMemberId
    );

    void petModifyV2(
            Long petId,
            PetModifyReqDto petRegisterDto,
            Long authMemberId
    );

    void petDelete(
            Long petId,
            Long authMemberId
    );
}
