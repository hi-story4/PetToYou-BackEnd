package com.pettoyou.server.domains.reservation.dto.response;

import com.pettoyou.server.domains.reservation.entity.TimeTable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link com.pettoyou.server.domains.reservation.entity.TimeTable}
 */
@Builder
public record TimeTableRespDto(Long timeTableId, @NotNull Long vetId, @NotNull Long storeId,
                               @NotNull LocalDate reservationDate, @NotNull LocalTime reservationStartTime,
                               @NotNull LocalTime reservationEndTime)  {

    public static TimeTableRespDto toDto(TimeTable timeTable) {
        return TimeTableRespDto.builder()
                .timeTableId(timeTable.getTimeTableId())
                .vetId(timeTable.getVetId())
                .storeId(timeTable.getStoreId())
                .reservationDate(timeTable.getReservationDate())
                .reservationStartTime(timeTable.getReservationStartTime())
                .reservationEndTime(timeTable.getReservationEndTime())
                .build();

    }
}