package com.pettoyou.server.domains.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE store_photo SET photo_status='DEACTIVATE' WHERE store_photo_id=?")
@SQLRestriction("photo_status = 'ACTIVATE'")
@Table(name = "store_photo")
public class StorePhoto extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_photo_id")
    private Long storePhotoId;

    @Embedded
    @NotNull
    private PhotoData storePhoto;

    @NotNull
    private Integer photoOrder;

    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    private BaseStatus photoStatus = BaseStatus.ACTIVATE;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;

}
