package com.pettoyou.server.domains.reserve.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.reserve.entity.enums.ReserveStatus;
import com.pettoyou.server.domains.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "reserve")
public class Reserve extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Long reserveId;

    private LocalDateTime reserveTime;
    //Date와 Time 분리????

    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}