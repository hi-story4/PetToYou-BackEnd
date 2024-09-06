package com.pettoyou.server.domains.hospital.entity.hospitalAdmin;

import com.pettoyou.server.constant.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hospital_admin")
public class HospitalAdmin extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_admin_id")
    private Long hospitalAdminId;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Long hospitalId;

    @NotNull
    @OneToMany(mappedBy = "hospitalAdmin")
    private List<HospitalAdminRole> roles = new ArrayList<>();

    @Builder
    private HospitalAdmin(String username, String password, Long hospitalId) {
        this.username = username;
        this.password = password;
        this.hospitalId = hospitalId;
    }

    public static HospitalAdmin of(String username, String password, Long hospitalId) {
        return HospitalAdmin.builder()
                .username(username)
                .password(password)
                .hospitalId(hospitalId)
                .build();
    }
}
