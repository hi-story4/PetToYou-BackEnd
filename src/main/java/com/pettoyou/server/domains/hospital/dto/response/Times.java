package com.pettoyou.server.domains.hospital.dto.response;

import com.pettoyou.server.domains.store.entity.BusinessHour;
import lombok.Builder;

import java.sql.Time;

@Builder
public record Times(
        boolean openSt,
        Time startTime,
        Time endTime,
        Time breakStartTime,
        Time breakEndTime,
        Time registrationClose
) {
    public static Times of(BusinessHour businessHour){
        return Times.builder()
                .openSt(businessHour.isOpenSt())
                .startTime(businessHour.getStartTime())
                .endTime(businessHour.getEndTime())
                .breakStartTime(businessHour.getBreakStartTime())
                .breakEndTime(businessHour.getBreakEndTime())
                .registrationClose(businessHour.getRegistrationClose())
                .build();
    }
}
