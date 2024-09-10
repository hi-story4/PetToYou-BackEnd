package com.pettoyou.server.domains.hospital.entity.hospital;


import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.review.entity.Review;
import com.pettoyou.server.domains.store.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("H")
@OnDelete(action = OnDeleteAction.CASCADE)
// store를 soft-delete 했을때 상속 객체인 hospital도 똑같이 soft-delete 해주도록 설정
@Table(name = "hospital")
public class Hospital extends Store {

    // 병원의 기본 정보 설명 (태그에 담기지 못하는 애들)
    // 주차가능, 제로페이 가능 등등...
    private String additionalServiceTag;

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TagMapper> tags;

    @Builder()
    public Hospital(Long storeId, String storeName, String storePhone, PhotoData thumbnail, String notice, Address address,
                    String websiteLink, String storeInfo, PhotoData storeInfoPhoto, String additionalServiceTag, RegistrationInfo registrationInfo, List<BusinessHour> businessHours, List<Review> reviews, List<StorePhoto> storePhotos
    ) {
        super(storeId, storeName, storePhone, thumbnail, notice, address, websiteLink, storeInfo, storeInfoPhoto, registrationInfo, businessHours, reviews, storePhotos);
        this.additionalServiceTag = additionalServiceTag;
        this.tags = new ArrayList<>();
        //기본값 적용.
    }
}

