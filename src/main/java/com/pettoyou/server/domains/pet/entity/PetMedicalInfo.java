package com.pettoyou.server.domains.pet.entity;

import com.pettoyou.server.domains.pet.dto.request.PetMedicalInfoDto;
import com.pettoyou.server.domains.pet.entity.enums.Bmi;
import com.pettoyou.server.domains.pet.entity.enums.NeuteringStatus;
import com.pettoyou.server.domains.pet.entity.enums.VaccinationStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PetMedicalInfo {
    private Double weight;

    @Enumerated(EnumType.STRING)
    private Bmi bmi;

    private String registerNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NeuteringStatus neuteringStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VaccinationStatus vaccinationStatus;

    private String allergy;

    private String currentFeedName;

    private String medicalHistory;

    public static PetMedicalInfo from(PetMedicalInfoDto petMedicalInfoDto) {
        return builder()
                .weight(petMedicalInfoDto.weight())
                .bmi(petMedicalInfoDto.bmi())
                .registerNumber(petMedicalInfoDto.registerNumber())
                .neuteringStatus(petMedicalInfoDto.neuteringStatus())
                .vaccinationStatus(petMedicalInfoDto.vaccinationStatus())
                .allergy(petMedicalInfoDto.allergy())
                .currentFeedName(petMedicalInfoDto.currentFeedName())
                .medicalHistory(petMedicalInfoDto.medicalHistory())
                .build();
    }

    public String getFormatWeight() {
        if (this.weight == null) {
            return null;
        }

        if (this.weight % 1 != 0) return String.valueOf(this.weight);
        return String.valueOf(this.weight.intValue());
    }
}