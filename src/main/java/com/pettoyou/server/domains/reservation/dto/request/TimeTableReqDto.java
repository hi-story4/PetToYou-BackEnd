package com.pettoyou.server.domains.reservation.dto.request;

import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.reservation.entity.TimeTable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link com.pettoyou.server.domains.reservation.entity.TimeTable}
 */

public record TimeTableReqDto(@NotNull Long storeId, @NotNull Long vetId, @NotNull @FutureOrPresent LocalDate reservationDate,
                              @NotNull @FutureOrPresent LocalTime reservationStartTime,
                              @NotNull @Future LocalTime reservationEndTime)  {

    public static TimeTable toEntity(TimeTableReqDto timeTableReqDto){
        return TimeTable.builder()
                .reservationDate(timeTableReqDto.reservationDate)
                .reservationStartTime(timeTableReqDto.reservationStartTime)
                .reservationEndTime(timeTableReqDto.reservationEndTime)
                .storeId(timeTableReqDto.storeId)
                .vetId(timeTableReqDto.vetId)
                .activeStatus(BaseStatus.ACTIVATE)
                .build();


    }
}