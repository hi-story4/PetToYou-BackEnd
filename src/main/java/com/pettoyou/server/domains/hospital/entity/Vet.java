package com.pettoyou.server.domains.hospital.entity;

import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE vet SET vet_status = 'DEACTIVATE' WHERE vet_id = ?")
@SQLRestriction("vet_status = 'ACTIVATE'")
@Entity
public class Vet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vet_id")
    private Long id;

    @NotNull
    private String vetName;

    private String experience;

    private String specialities;

    private String introduction;

    @NotNull
    private Long hospitalId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BaseStatus vetStatus;

    @Embedded
    private PhotoData profiilePhotoData;

}
