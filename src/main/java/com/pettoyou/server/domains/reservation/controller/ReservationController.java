package com.pettoyou.server.domains.reservation.controller;

import com.pettoyou.server.config.security.service.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;
import com.pettoyou.server.domains.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<Object>> reservationRegist(
            @RequestBody ReservationRegistReqDto reservationRegistReqDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        reservationService.reservationRegist(reservationRegistReqDto, principalDetails.getUserId());
        return ApiResponse.createSuccessWithOk(null);
    }

}
