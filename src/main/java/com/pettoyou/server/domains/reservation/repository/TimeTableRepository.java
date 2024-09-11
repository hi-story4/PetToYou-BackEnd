package com.pettoyou.server.domains.reservation.repository;

import com.pettoyou.server.domains.reservation.entity.TimeTable;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationTimeStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    Optional<TimeTable> findTimeTableByTimeTableIdAndStoreIdAndAvailableStatus(Long timeTableId, Long storeId, @NotNull ReservationTimeStatus availableStatus);
}