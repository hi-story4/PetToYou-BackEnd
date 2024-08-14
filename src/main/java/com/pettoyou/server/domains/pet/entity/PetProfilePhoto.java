package com.pettoyou.server.domains.pet.entity;

import com.pettoyou.server.domains.photo.entity.PhotoData;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "pet_profile_photo")
public class PetProfilePhoto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petProfilePhotoId;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Embedded
    private PhotoData photoData;

    public static PetProfilePhoto toPetProfilePhoto(PhotoData photoData, Pet pet) {
        return builder()
                .photoData(photoData)
                .pet(pet)
                .build();
    }
}
