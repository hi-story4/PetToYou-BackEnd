package com.pettoyou.server.domains.hospital.entity.hospitalAdmin;

import com.pettoyou.server.domains.member.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HospitalAdminRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalAdminRoleId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "hospital_admin_id")
    private HospitalAdmin hospitalAdmin;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "role_id")
    private Role role;

    public static HospitalAdminRole of (HospitalAdmin hospitalAdmin, Role role) {
        return HospitalAdminRole.builder()
                .hospitalAdmin(hospitalAdmin)
                .role(role)
                .build();
    }
}
