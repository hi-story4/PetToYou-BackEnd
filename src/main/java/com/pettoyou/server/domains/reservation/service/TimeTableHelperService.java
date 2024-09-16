package com.pettoyou.server.domains.reservation.service;


import com.pettoyou.server.domains.reservation.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeTableHelperService {

    private final TimeTableRepository timeTableRepository;

    public boolean timeTableExistsWithDateAndTime(LocalDate date, LocalTime time) {
        return timeTableRepository.existsByReservationDateAndReservationStartTime(date, time);
    }
}
