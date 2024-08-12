package com.pettoyou.server.domains.photo.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoData {
    private String bucket;

    private String object;

    private String photoUrl;

    public static PhotoData of(String bucket, String object, String photoUrl) {
        return new PhotoData(bucket, object, photoUrl);
    }

    // Todo : 펫 기본이미지가 나왔을 경우에 바꿔줘야함. -> 그때는 YML에 저장해야함
    public static PhotoData generateDefaultPetProfilePhotoData() {
        return new PhotoData("tmpBucket", "tmpObject", "tmpPhotoUrl.com");
    }
}
