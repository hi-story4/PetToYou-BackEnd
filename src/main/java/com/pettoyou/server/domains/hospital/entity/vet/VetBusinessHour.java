package com.pettoyou.server.domains.hospital.entity.vet;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class VetBusinessHour {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vet_business_hour_id")
    private Long id;

    @NotNull
    private Integer dayOfWeek;

    @NotNull
    private boolean openSt;

    @NotNull
    private Long vetId;
}
