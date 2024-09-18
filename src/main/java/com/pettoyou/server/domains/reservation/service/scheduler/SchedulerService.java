package com.pettoyou.server.domains.reservation.service.scheduler;

import com.pettoyou.server.domains.reservation.entity.Reservation;

public interface SchedulerService {
    void scheduleReservationCompletion(Reservation reservation);

}
