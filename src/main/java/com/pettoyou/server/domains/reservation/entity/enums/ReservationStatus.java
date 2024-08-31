package com.pettoyou.server.domains.reservation.entity.enums;

public enum ReservationStatus {
    RESERVE_PENDING, // 예약 전
    RESERVE_COMPLETE, // 예약 완료
    RESERVE_CANCELED, // 예약 취소
    VISIT_COMPLETE, // 방문 완료
    NO_SHOW // 방문 미완료
}
