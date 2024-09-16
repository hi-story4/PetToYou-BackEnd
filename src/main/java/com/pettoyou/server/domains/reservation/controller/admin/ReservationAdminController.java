package com.pettoyou.server.domains.reservation.controller.admin;

import com.pettoyou.server.config.security.service.hospital.HospitalAdminDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.reservation.dto.request.ReservationStatusReqDto;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;
import com.pettoyou.server.domains.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final ReservationService reservationService;


    @PutMapping("/reservation")
    public ResponseEntity<ApiResponse<ReservationStatus>> reservationStatus(
            @RequestBody ReservationStatusReqDto reservationStatusReqDto,
            @AuthenticationPrincipal HospitalAdminDetails hospitalAdminDetails
    ) {
        ReservationStatus status = reservationService.updateReservationStatusByAdmin(reservationStatusReqDto, hospitalAdminDetails);
        return ApiResponse.createSuccessWithOk(status);

    }

}
