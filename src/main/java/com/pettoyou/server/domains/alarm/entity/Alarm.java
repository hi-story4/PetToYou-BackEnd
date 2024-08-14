package com.pettoyou.server.domains.alarm.entity;

import com.pettoyou.server.domains.alarm.entity.enums.AlarmType;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "alarm")
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String alarmTitle;

    private String alarmContent;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Enumerated(EnumType.STRING)
    private BaseStatus alarmStatus;

}

//
//AlarmId long PK IDENTITY
//MemberId long FK >- Member.MemberId
//AlarmTitle string
//AlarmContent string
//AlarmType string # RESERVE_COMPLETE ...
//AlarmStatus string # ACTIVATE, DEACTIVATE,
