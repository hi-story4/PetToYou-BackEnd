package com.pettoyou.server.domains.reservation.service;


import com.pettoyou.server.domains.reservation.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimeTableHelperService {

    private final TimeTableRepository timeTableRepository;

    public boolean timeTableExistsWithVetIdAndDateTime(Long vetId, LocalDateTime dateTime) {
        return timeTableRepository.existsByVetIdAndReservationStartDateTime(vetId, dateTime);
    }
}
