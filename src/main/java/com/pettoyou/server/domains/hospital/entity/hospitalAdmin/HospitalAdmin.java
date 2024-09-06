package com.pettoyou.server.domains.hospital.entity.hospitalAdmin;

import com.pettoyou.server.constant.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HospitalAdmin extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalAdminId;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Long hospitalId;

    @NotNull
    @OneToMany(mappedBy = "hospital_admin")
    private List<HospitalAdminRole> roles = new ArrayList<>();
}
