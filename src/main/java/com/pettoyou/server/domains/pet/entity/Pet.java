package com.pettoyou.server.domains.pet.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.BaseStatus;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.pet.dto.request.PetModifyReqDto;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterReqDto;
import com.pettoyou.server.domains.pet.entity.enums.Species;
import com.pettoyou.server.domains.pet.dto.request.PetRegisterAndModifyReqDto;
import com.pettoyou.server.domains.pet.entity.enums.Gender;
import com.pettoyou.server.domains.pet.entity.enums.PetType;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.reservation.entity.Reserve;
import com.pettoyou.server.domains.review.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE pet SET pet_status = 'DEACTIVATE' WHERE pet_id = ?")
@SQLRestriction("pet_status = 'ACTIVATE'")
@Table(name = "pet")
public class Pet extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @NotNull
    @Column(nullable = false)
    private String petName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Species species;


    @NotNull
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PetType petType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    private LocalDate adoptionDate;

    private String caution;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BaseStatus petStatus;

    @Embedded
    private PhotoData profilePhotoData;

    @NotNull
    @Embedded
    private PetMedicalInfo petMedicalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Reserve> reserves = new ArrayList<>();

    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    public static Pet of(PetRegisterAndModifyReqDto registerDto, PhotoData profilePhotoData, Member member) {
        return builder()
                .petName(registerDto.petName())
                .species(registerDto.species())
                .birth(registerDto.birth())
                .petType(registerDto.petType())
                .gender(registerDto.gender())
                .adoptionDate(registerDto.adoptionDate() == null ? null : registerDto.adoptionDate())
                .petMedicalInfo(PetMedicalInfo.from(registerDto.petMedicalInfoDto()))
                .member(member)
                .petStatus(BaseStatus.ACTIVATE)
                .caution(registerDto.caution())
                .profilePhotoData(profilePhotoData)
                .build();
    }

    public static Pet ofV2(PetRegisterReqDto registerDto, PhotoData profilePhotoData, Member member) {
        return builder()
                .petName(registerDto.petName())
                .species(registerDto.species())
                .birth(registerDto.birth())
                .petType(registerDto.petType())
                .gender(registerDto.gender())
                .adoptionDate(registerDto.adoptionDate() == null ? null : registerDto.adoptionDate())
                .petMedicalInfo(PetMedicalInfo.from(registerDto.petMedicalInfoDto()))
                .member(member)
                .petStatus(BaseStatus.ACTIVATE)
                .caution(registerDto.caution())
                .profilePhotoData(profilePhotoData)
                .build();
    }

    public void modify(PetRegisterAndModifyReqDto modifyDto, PhotoData newPhoto) {
        this.petName = modifyDto.petName();
        this.species = modifyDto.species();
        this.birth = modifyDto.birth();
        this.petType = modifyDto.petType();
        this.adoptionDate = modifyDto.adoptionDate();
        this.caution = modifyDto.caution();
        this.petMedicalInfo = PetMedicalInfo.from(modifyDto.petMedicalInfoDto());
        this.profilePhotoData = newPhoto;
    }


    public void modifyV2(PetModifyReqDto modifyDto, PhotoData newPhoto) {
        this.petName = modifyDto.petName();
        this.species = modifyDto.species();
        this.birth = modifyDto.birth();
        this.petType = modifyDto.petType();
        this.adoptionDate = modifyDto.adoptionDate();
        this.caution = modifyDto.caution();
        this.petMedicalInfo = PetMedicalInfo.from(modifyDto.petMedicalInfoDto());
        this.profilePhotoData = newPhoto;
    }

    public String getProfileImgUrl() {
        return profilePhotoData.getPhotoUrl();
    }

    public int petAgeCalculate(LocalDate currentLocalDate) {
        Period period = Period.between(this.birth, currentLocalDate);
        return period.getYears() * 12 + period.getMonths();
    }

    public String getGenderLabel() {
        if (gender == Gender.MALE) {
            return "남아";
        } else if (gender == Gender.FEMALE) {
            return "여아";
        } else {
            return "알수없음";
        }
    }

    public void validateOwnerAuthorization(Long authMemberId) {
        if (!this.member.getMemberId().equals(authMemberId)) {
            throw new CustomException(CustomResponseStatus.MEMBER_NOT_MATCH);
        }
    }

    public String getPetWeight() {
        return petMedicalInfo.getFormatWeight();
    }

    public String getKoreanSpeciesName() {
        return species.getKoreanName();
    }
}