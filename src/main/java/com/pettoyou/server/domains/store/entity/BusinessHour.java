package com.pettoyou.server.domains.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pettoyou.server.constant.entity.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Time;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "business_hour")
public class BusinessHour extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_hour_id")
    private Long businessHourId;
    @NotNull
    private Integer dayOfWeek;
    @NotNull
    private Time startTime;
    @NotNull
    private Time endTime;

    @Nullable
    private Time breakStartTime;

    @Nullable
    private Time breakEndTime;

    @NotNull
    private boolean openSt;
    //business_hour은 각각, 월화수목금토일의 상태를 나타내고 openSt=false;는 정기휴무날임을 뜻한다.

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;
}