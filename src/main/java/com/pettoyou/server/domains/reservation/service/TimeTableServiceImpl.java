package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.reservation.dto.request.TimeTableReqDto;
import com.pettoyou.server.domains.reservation.entity.TimeTable;
import com.pettoyou.server.domains.reservation.repository.TimeTableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeTableServiceImpl implements TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final TimeTableHelperService timeTableHelperService;

    public TimeTable postTimeTable(TimeTableReqDto timeTableReqDto) {

        if (timeTableHelperService.timeTableExistsWithVetIdAndDateTime(timeTableReqDto.vetId(), timeTableReqDto.reservationStartDateTime())) {
            return timeTableRepository.save(TimeTableReqDto.toEntity(timeTableReqDto));
        }
        else throw new CustomException(CustomResponseStatus.RESERVATION_ALREADY_EXIST);
    }




}
