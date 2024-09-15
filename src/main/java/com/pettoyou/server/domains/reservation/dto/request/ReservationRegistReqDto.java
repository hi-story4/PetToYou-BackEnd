package com.pettoyou.server.domains.reservation.dto.request;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ReservationRegistReqDto(
        LocalDate reservationDate,//예약 날짜
        LocalTime reservationTime, //예약 시간

        Long storeId, // 예약한 스토어
        Long vetId, // 예약한 수의사

        Long petId, // 방문하는 반려동물
        String medicalService, // 진료 항목
        String caution //주의 사항
) {
}
