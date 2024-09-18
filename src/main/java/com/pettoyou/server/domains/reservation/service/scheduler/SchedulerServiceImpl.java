package com.pettoyou.server.domains.reservation.service.scheduler;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.reservation.entity.Reservation;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;
import com.pettoyou.server.domains.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;
    private final ReservationRepository reservationRepository;
    // Store ScheduledFutures in case you need to cancel them later
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    public void scheduleReservationCompletion(Reservation reservation) {

        LocalDateTime reservationDateTime = reservation.getReservationDateTime();
        log.info("Scheduling reservationTime: " + reservationDateTime);

        // 예약 시간이 지났을 경우 상태를 업데이트
        if (reservationDateTime.isAfter(LocalDateTime.now())) {

            // 예약 시간이 미래일 경우 작업을 예약
            log.info("예약시간이 미래일 경우 작업 예약");
            log.info("시간: " + reservationDateTime.toInstant(ZoneOffset.UTC).toString());
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                    () -> updateReservationStatusToCompleted(reservation.getReservationId()), // The task to execute
                    Date.from(reservationDateTime.atZone(ZoneOffset.systemDefault()).toInstant())
            );
            // Store the ScheduledFuture to track or cancel later
            scheduledTasks.put(reservation.getReservationId(), scheduledTask);

        } else {
            log.info("예약 시간이 지나서 상태 업데이트.");
            throw new CustomException(CustomResponseStatus.INVALID_RESERVATION_DATETIME_ERROR);
        }
    }


    // 예약 상태를 'COMPLETED'로 변경하는 메서드
    private void updateReservationStatusToCompleted(Long reservationId) {
        log.info("예약된 코드: Reservation Status to Be Completed");
        reservationRepository.updateReservationStatusByReservationId(ReservationStatus.VISIT_COMPLETE, reservationId);
    }
}


