package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.config.security.service.hospital.HospitalAdminDetails;
import com.pettoyou.server.config.security.service.member.PrincipalDetails;
import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;
import com.pettoyou.server.domains.reservation.dto.request.ReservationStatusReqDto;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;

public interface ReservationService {

    void reservationRegist(
            ReservationRegistReqDto registReqDto,
            Long authMemberId
    );


    ReservationStatus updateReservationStatusByAdmin(ReservationStatusReqDto reservationStatusReqDto, HospitalAdminDetails hospitalAdminDetails);
    ReservationStatus updateReservationStatusByUser(ReservationStatusReqDto reservationStatusReqDto, PrincipalDetails principalDetails);


}
