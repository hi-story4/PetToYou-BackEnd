package com.pettoyou.server.domains.reservation.repository.custom;

import com.pettoyou.server.domains.reservation.entity.Reservation;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.pettoyou.server.domains.reservation.entity.QReservation.*;

@Repository
@RequiredArgsConstructor
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Reservation findByStoreIdAndReserveDateAndReserveStartAndEndTimeAndReserveStatus(
            Long storeId,
            LocalDate reservationDate,
            LocalTime reservationStartTime,
            LocalTime reservationEndTime
    ) {
        return jpaQueryFactory
                .select(reservation)
                .from(reservation)
                .where(
                        reservation.storeId.eq(storeId),
                        reservation.reservationDate.eq(reservationDate),
                        reservation.reservationStartTime.eq(reservationStartTime),
                        reservation.reservationEndTime.eq(reservationEndTime),
                        reservation.reservationStatus.ne(ReservationStatus.RESERVE_CANCELED)
                )
                .fetchOne();
    }
}
