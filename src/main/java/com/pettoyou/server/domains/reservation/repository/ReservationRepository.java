package com.pettoyou.server.domains.reservation.repository;

import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.reservation.entity.Reservation;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {

    @Transactional
    @Modifying
    @Query("update Reservation r set r.reservationStatus = ?1 where r.reservationId = ?2")
    int updateReservationStatusByReservationId(ReservationStatus reservationStatus, Long reservationId);

    @Transactional
    @Modifying
    @Query("update Reservation r set r.activeStatus = ?1 where r.reservationId = ?2 and r.reservationStatus = ?3")
    int updateActiveStatusByReservationIdAndReservationStatus(BaseStatus activeStatus, Long reservationId, ReservationStatus reservationStatus);

    @Transactional
    @Modifying
    @Query("update Reservation r set r.reservationStatus = ?1 where r.reservationId = ?2 and r.reservationStatus = ?3")
    int updateReservationStatusByReservationIdAndReservationStatus(ReservationStatus reservationStatus, Long reservationId, ReservationStatus reservationStatus1);
}
