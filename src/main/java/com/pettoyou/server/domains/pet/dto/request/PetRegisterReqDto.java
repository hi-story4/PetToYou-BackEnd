package com.pettoyou.server.domains.pet.dto.request;

import com.pettoyou.server.domains.pet.entity.enums.Gender;
import com.pettoyou.server.domains.pet.entity.enums.PetType;
import com.pettoyou.server.domains.pet.entity.enums.Species;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PetRegisterReqDto(
        @NotNull(message = "반려동물의 타입(강아지 or 고양이)을 선택해주세요.")
        PetType petType,
        @NotBlank(message = "반려동물의 이름을 입력해주세요.")
        String petName,
        @NotNull(message = "반려동물의 생일을 입력해주세요.")
        LocalDate birth,
        LocalDate adoptionDate,
        @NotNull(message = "반려동물의 성별을 선택해주세요.")
        Gender gender,
        @NotNull(message = "반려동물의 품종을 선택해주세요.")
        Species species,
        String caution,
        PetMedicalInfoDto petMedicalInfoDto,
        PetProfilePhotoDto petProfilePhotoDto
) {
}
