package com.pettoyou.server.domains.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberRole {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberRoleId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "role_id")
    private Role role;

    public static MemberRole of (Member member, Role role) {
        return MemberRole.builder()
                .member(member)
                .role(role)
                .build();
    }
}
