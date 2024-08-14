package com.pettoyou.server.domains.healthNote.dto.response;

import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HealthNoteSimpleInfoDto(
        String hospitalName,
        String medicalRecord,
        LocalDate visitDate,
        String caution
) {

    public static HealthNoteSimpleInfoDto of(HealthNote healthNote, String hospitalName) {
        return HealthNoteSimpleInfoDto.builder()
                .hospitalName(hospitalName)
                .medicalRecord(healthNote.getMedicalRecord())
                .visitDate(healthNote.getVisitDate())
                .caution(healthNote.getCaution())
                .build();
    }
}
