package com.pettoyou.server.domains.reservation.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationTimeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import software.amazon.awssdk.services.s3.model.ReplicationTimeStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE time_table SET active_status = 'DEACTIVATE' WHERE reservation_id = ?")
@SQLRestriction("active_status = 'ACTIVATE'")
@Table(name = "time_table")
public class TimeTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "time_table_id")
    private Long timeTableId;

    @NotNull
    private Long vetId;

    @NotNull
    private Long storeId;

    @NotNull
    private LocalDate reservationDate; // 진료 날짜 (YY:MM:DD)

    @NotNull
    private LocalTime reservationStartTime; // 예약 시작 시간 (HH:MM)

    @NotNull
    private LocalTime reservationEndTime; // 예약 마감 시간 (HH:MM)

    @Enumerated(EnumType.STRING)
    private BaseStatus activeStatus;
}

