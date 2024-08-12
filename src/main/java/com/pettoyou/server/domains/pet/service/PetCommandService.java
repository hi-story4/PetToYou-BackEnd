package com.pettoyou.server.domains.pet.service;

import com.pettoyou.server.domains.pet.dto.request.PetRegisterAndModifyReqDto;
import com.pettoyou.server.domains.pet.dto.response.PetRegisterRespDto;
import org.springframework.web.multipart.MultipartFile;

public interface PetCommandService {
    PetRegisterRespDto petRegister(
            MultipartFile petProfileImgs,
            PetRegisterAndModifyReqDto petRegisterDto,
            Long loginMemberId);

    void petModify(
            Long petId,
            MultipartFile petProfileImg,
            PetRegisterAndModifyReqDto petRegisterDto,
            Long loginMemberId
    );

    void petDelete(
            Long petId,
            Long loginMemberId
    );
}
