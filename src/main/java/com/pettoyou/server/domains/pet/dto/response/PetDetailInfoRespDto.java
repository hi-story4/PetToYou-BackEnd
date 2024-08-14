package com.pettoyou.server.domains.pet.dto.response;

import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.entity.PetMedicalInfo;
import com.pettoyou.server.domains.pet.entity.enums.PetType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PetDetailInfoRespDto(
        Long petId,
        String petName,
        String species,
        LocalDate birth,
        int age,
        String gender,
        String caution,
        PetType petType,
        LocalDate adoptionDate,
        PetMedicalInfo petMedicalInfo,
        String profileImgUrl
) {
    public static PetDetailInfoRespDto from(Pet pet) {
        return PetDetailInfoRespDto.builder()
                .petId(pet.getPetId())
                .petName(pet.getPetName())
                .species(pet.getKoreanSpeciesName())
                .birth(pet.getBirth())
                .age(pet.petAgeCalculate(LocalDate.now()))
                .gender(pet.getGenderLabel())
                .caution(pet.getCaution())
                .petType(pet.getPetType())
                .adoptionDate(pet.getAdoptionDate())
                .petMedicalInfo(pet.getPetMedicalInfo())
                .profileImgUrl(pet.getProfileImgUrl())
                .build();
    }
}
