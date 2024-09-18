package com.pettoyou.server.domains.reservation.dto.response;

import com.pettoyou.server.domains.reservation.entity.TimeTable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.pettoyou.server.domains.reservation.entity.TimeTable}
 */
@Builder
public record TimeTableRespDto(Long timeTableId, @NotNull Long vetId, @NotNull Long storeId,
                                @NotNull LocalDateTime reservationStartDateTime,
                               @NotNull LocalDateTime reservationEndDateTime)  {

    public static TimeTableRespDto toDto(TimeTable timeTable) {
        return TimeTableRespDto.builder()
                .timeTableId(timeTable.getTimeTableId())
                .vetId(timeTable.getVetId())
                .storeId(timeTable.getStoreId())
                .reservationStartDateTime(timeTable.getReservationStartDateTime())
                .reservationEndDateTime(timeTable.getReservationEndDateTime())
                .build();

    }
}