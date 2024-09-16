package com.pettoyou.server.domains.reservation.repository;

import com.pettoyou.server.domains.reservation.entity.TimeTable;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationTimeStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {


   boolean existsByReservationDateAndReservationStartTime(LocalDate date, LocalTime time);

   Optional<TimeTable> findTimeTableByReservationDateAndReservationStartTime(LocalDate date, LocalTime time);
}