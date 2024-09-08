package com.pettoyou.server.domains.store.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.review.entity.Review;
import com.pettoyou.server.domains.store.entity.enums.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE store SET store_status = 'DEACTIVATE' WHERE store_id=?")
@SQLRestriction("store_status = 'ACTIVATE'")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // 하위 테이블의 구분 컬럼 생성
@Table(name = "store", indexes = {
        @Index(name = "idx_store_name", columnList = "storeName")
})
public abstract class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;

    @NotNull
    @Size(min = 2)
    private String storeName;

    @NotNull
    private String storePhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bucket", column = @Column(name = "thumbnail_bucket")),
            @AttributeOverride(name = "object", column = @Column(name = "thumbnail_object")),
            @AttributeOverride(name = "photoUrl", column = @Column(name = "thumbnail_photo_url"))
    })
    private PhotoData thumbnail;

    private String notice;

    private String websiteLink;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @Column(columnDefinition = "TEXT")
    private String storeInfo;

    @Embedded
    private PhotoData storeInfoPhoto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BaseStatus storeStatus;

    @Embedded
    @NotNull
    private Address address;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BusinessHour> businessHours = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<StorePhoto> storePhotos = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "registration_info_id")
    private RegistrationInfo registrationInfo;

    protected Store(
            Long storeId, String storeName, String storePhone, PhotoData thumbnail,
            String notice, Address address, String websiteLink, String storeInfo,
            PhotoData storeInfoPhoto, RegistrationInfo registrationInfo,
            List<BusinessHour> businessHours, List<Review> reviews, List<StorePhoto> storePhotos
    ) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.thumbnail = thumbnail;
        this.notice = notice;
        this.address = address;
        this.websiteLink = websiteLink;
        this.storeInfo = storeInfo;
        this.storeInfoPhoto = storeInfoPhoto;
        this.registrationInfo = registrationInfo;
        this.businessHours = businessHours;
        this.reviews = reviews;
        this.storePhotos = storePhotos;
        //기본값 적용.
        this.subscriptionStatus = SubscriptionStatus.UNSUBSCRIBED;
        this.storeStatus = BaseStatus.ACTIVATE;
    }
}
