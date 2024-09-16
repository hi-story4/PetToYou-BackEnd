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
    private Long reservationId;

    private String medicalService; // 진료 항목

    @NotNull
    private LocalDate reservationDate;

    @NotNull
    private LocalTime reservationTime;

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
    public Reservation(String medicalService, LocalDate reservationDate, LocalTime reservationTime,  ReservationStatus reservationStatus, BaseStatus activeStatus, Long storeId, Long petId, Long memberId, Long vetId) {
        this.medicalService = medicalService;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationStatus = reservationStatus;
        this.activeStatus = activeStatus;
        this.storeId = storeId;
        this.petId = petId;
        this.memberId = memberId;
        this.vetId = vetId;
    }

    public static Reservation of(
            ReservationRegistReqDto reservation,
            Long memberId
    ) {
        return Reservation.builder()
                .medicalService(reservation.medicalService())
                .reservationDate(reservation.reservationDate())
                .reservationTime(reservation.reservationTime())
                .reservationStatus(ReservationStatus.RESERVE_PENDING)
                .activeStatus(BaseStatus.ACTIVATE)
                .storeId(reservation.storeId())
                .petId(reservation.petId())
                .memberId(memberId)
                .vetId(reservation.vetId())
                .build();
    }
    
    public static Reservation modifyReserationStatus(Reservation reservation, ReservationStatus status) {
        return Reservation.builder()
                .medicalService(reservation.getMedicalService())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationStatus(status)
                .activeStatus(BaseStatus.ACTIVATE)
                .storeId(reservation.getStoreId())
                .petId(reservation.getPetId())
                .memberId(reservation.getMemberId())
                .vetId(reservation.getVetId())
                .build();
    }
}