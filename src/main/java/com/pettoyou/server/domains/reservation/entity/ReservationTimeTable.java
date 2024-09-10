package com.pettoyou.server.domains.reservation.entity;


import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "reservation_time_table")
@Getter
public class ReservationTimeTable {
    @Id
    private String id; // MongoDB에서 사용하는 고유 ID

    @NotNull
    private LocalDate reservationDate;

    @NotNull
    private LocalTime reservationStartTime;

    @NotNull
    private LocalTime reservationEndTime;

    @NotNull
    private String memberName;

    @NotNull
    private String phone;

    @NotNull
    private String medicalService;

    @NotNull
    private Long storeId;

    @NotNull
    private Long vetId;
}
