package com.pettoyou.server.domains.store.entity;

import com.pettoyou.server.domains.member.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
public class StoreMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeMemberId;

    @NotNull
    private String nickname; // 병원 이름 들어갈 계획

    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private Long storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
