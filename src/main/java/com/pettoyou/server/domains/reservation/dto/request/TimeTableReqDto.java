package com.pettoyou.server.domains.reservation.dto.request;

import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.reservation.entity.TimeTable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link com.pettoyou.server.domains.reservation.entity.TimeTable}
 */

public record TimeTableReqDto(@NotNull Long storeId, @NotNull Long vetId,
                              @NotNull @FutureOrPresent LocalDateTime reservationStartDateTime,
                              @NotNull @Future LocalDateTime reservationEndDateTime)  {

    public static TimeTable toEntity(TimeTableReqDto timeTableReqDto){
        return TimeTable.builder()
                .reservationStartDateTime(timeTableReqDto.reservationStartDateTime)
                .reservationEndDateTime(timeTableReqDto.reservationEndDateTime)
                .storeId(timeTableReqDto.storeId)
                .vetId(timeTableReqDto.vetId)
                .activeStatus(BaseStatus.ACTIVATE)
                .build();


    }
}