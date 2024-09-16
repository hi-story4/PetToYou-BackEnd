package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.domains.reservation.dto.request.TimeTableReqDto;
import com.pettoyou.server.domains.reservation.entity.TimeTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface TimeTableService {

    TimeTable postTimeTable(TimeTableReqDto timeTableReqDto);

}
