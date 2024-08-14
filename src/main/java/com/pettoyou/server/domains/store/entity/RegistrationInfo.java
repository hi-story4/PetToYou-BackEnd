package com.pettoyou.server.domains.store.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.domains.store.entity.enums.StoreType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "registration_info")
public class RegistrationInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_info_id")
    private Long registrationInfoId;


    @Enumerated(EnumType.STRING)
    private StoreType storeType;
    @NotNull
    private String ceoName;
    private String ceoPhone;
    @NotNull
    private String ceoEmail;
    @NotNull
    private String businessNumber;
}