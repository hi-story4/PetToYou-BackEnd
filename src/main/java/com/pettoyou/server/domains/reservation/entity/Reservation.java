package com.pettoyou.server.domains.reservation.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE reservation SET active_status = 'DEACTIVATE' WHERE reservation_id = ?")
@SQLRestriction("active_status = 'ACTIVATE'")
@Table(name = "reservation")
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private String medicalService; // 진료 항목

    @NotNull
    private LocalDate reservationDate; // 진료 날짜 (YY:MM:DD)

    @NotNull
    private LocalTime reservationStartTime; // 예약 시작 시간 (HH:MM)

    @NotNull
    private LocalTime reservationEndTime; // 예약 마감 시간 (HH:MM)

    @Enumerated(EnumType.STRING)
    @NotNull
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BaseStatus activeStatus;

    @NotNull
    private Long storeId;

    @NotNull
    private Long petId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long vetId;

    @Builder
    public Reservation(String medicalService, LocalDate reservationDate, LocalTime reservationStartTime, LocalTime reservationEndTime, ReservationStatus reservationStatus, BaseStatus activeStatus, Long storeId, Long petId, Long memberId, Long vetId) {
        this.medicalService = medicalService;
        this.reservationDate = reservationDate;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.reservationStatus = reservationStatus;
        this.activeStatus = activeStatus;
        this.storeId = storeId;
        this.petId = petId;
        this.memberId = memberId;
        this.vetId = vetId;
    }

    public static Reservation of(
            ReservationRegistReqDto registReqDto,
            Long memberId
    ) {
        return Reservation.builder()
                .medicalService(registReqDto.medicalService())
                .reservationDate(registReqDto.reservationDate())
                .reservationStartTime(registReqDto.reservationStartTime())
                .reservationEndTime(registReqDto.reservationEndTime())
                .reservationStatus(ReservationStatus.RESERVE_PENDING)
                .activeStatus(BaseStatus.ACTIVATE)
                .storeId(registReqDto.storeId())
                .petId(registReqDto.petId())
                .memberId(memberId)
                .vetId(registReqDto.vetId())
                .build();
    }
}