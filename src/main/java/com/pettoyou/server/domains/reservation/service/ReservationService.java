package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;

public interface ReservationService {

    void reservationRegist(
            ReservationRegistReqDto registReqDto,
            Long authMemberId
    );
}
