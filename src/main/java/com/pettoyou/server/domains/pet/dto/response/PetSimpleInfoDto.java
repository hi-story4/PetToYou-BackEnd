package com.pettoyou.server.domains.pet.dto.response;

import com.pettoyou.server.domains.pet.entity.Pet;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PetSimpleInfoDto(
        Long petId,
        String profileImgUrl,
        String petName,
        String species,
        String gender,
        int age,
        String weight
) {
    public static PetSimpleInfoDto of(Pet pet) {
        return PetSimpleInfoDto.builder()
                .petId(pet.getPetId())
                .profileImgUrl(pet.getProfileImgUrl())
                .petName(pet.getPetName())
                .species(pet.getKoreanSpeciesName())
                .gender(pet.getGenderLabel())
                .age(pet.petAgeCalculate(LocalDate.now()))
                .weight(pet.getPetWeight())
                .build();
    }

}
