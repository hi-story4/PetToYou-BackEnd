package com.pettoyou.server.domains.reservation.dto.request;

import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;

public record ReservationStatusReqDto(

        Long reservationId ,
        ReservationStatus reservationStatus
) {
}
