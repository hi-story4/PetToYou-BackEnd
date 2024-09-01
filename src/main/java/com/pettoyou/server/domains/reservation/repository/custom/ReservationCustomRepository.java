package com.pettoyou.server.domains.reservation.repository.custom;

import com.pettoyou.server.domains.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationCustomRepository {
    Reservation findByStoreIdAndReserveDateAndReserveStartAndEndTimeAndReserveStatus(
            Long storeId,
            LocalDate reservationDate,
            LocalTime reservationStartTime,
            LocalTime reservationEndTime
    );
}
