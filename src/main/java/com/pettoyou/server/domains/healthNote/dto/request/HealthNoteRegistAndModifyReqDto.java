package com.pettoyou.server.domains.healthNote.dto.request;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HealthNoteRegistAndModifyReqDto(
        Long hospitalId,
        Long petId,
        LocalDate visitDate,
        String medicalRecord,
        String caution,
        @Nullable String vetName
) {
}
