package com.pettoyou.server.domains.reservation.controller;

import com.pettoyou.server.config.security.service.hospital.HospitalAdminDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.reservation.dto.request.TimeTableReqDto;
import com.pettoyou.server.domains.reservation.dto.response.TimeTableRespDto;
import com.pettoyou.server.domains.reservation.entity.TimeTable;
import com.pettoyou.server.domains.reservation.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping("/timetable")
    public ResponseEntity<ApiResponse<TimeTableRespDto>> timeTableRegist(
            TimeTableReqDto timeTableReqDto,
            HospitalAdminDetails hospitalAdminDetails
    )
    {


            TimeTable timeTable = timeTableService.postTimeTable(timeTableReqDto);
            return ApiResponse.createSuccessWithOk(TimeTableRespDto.toDto(timeTable));



    }

}
