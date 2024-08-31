package com.pettoyou.server.domains.reservation.repository;

import com.pettoyou.server.domains.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
