package com.pettoyou.server.domains.hospital.dto.request;

import com.pettoyou.server.domains.hospital.entity.Hospital;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.store.dto.request.AddressDto;
import com.pettoyou.server.domains.store.dto.BusinessHourDto;
import com.pettoyou.server.domains.store.dto.RegistrationInfoDto;
import com.pettoyou.server.domains.store.entity.enums.StoreType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Builder
public record HospitalDto(
        @NotNull(message = "병원 이름을 입력해주세요") @Size(min = 2, max = 20, message = "최소2글자, 최대20글자") String hospitalName,
        @NotNull(message = "올바른 전화번호 형식을 지켜주세요") @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$") String hospitalPhone,
        String notice,
        String additionalServiceTag,
        String websiteLink,
        String storeInfo,
        @NotNull AddressDto address,
        List<Long> tagIdList,
        List<BusinessHourDto.Request> businessHours,
        RegistrationInfoDto.Request registrationInfo
) {
    public static Hospital toHospitalEntity(HospitalDto hospitalDto, PhotoData thumbnail) {
        if (hospitalDto == null) {
            throw new IllegalArgumentException("HospitalDto is null");
        }
//        if (thumbnail == null) {
//            throw new IllegalArgumentException("Thumbnail is null");
//        }


            Hospital hospital = Hospital.builder()
                .additionalServiceTag(hospitalDto.additionalServiceTag())
                .storeName(hospitalDto.hospitalName())
                .storePhone(hospitalDto.hospitalPhone())
                .notice(hospitalDto.notice())
                .websiteLink(hospitalDto.websiteLink())
                .address(AddressDto.toEntity(hospitalDto.address()))
                .storeInfo(hospitalDto.storeInfo())
                .thumbnail(thumbnail)
                .storePhotos(new ArrayList<>())
                .businessHours(new ArrayList<>())
                .registrationInfo(RegistrationInfoDto.Request.toEntity(hospitalDto.registrationInfo(), StoreType.HOSPITAL))
                .build();

        hospital.getBusinessHours()
                .addAll(hospitalDto.businessHours()
                        .stream()
                        .map(businessHour -> BusinessHourDto.Request.toEntity(businessHour, hospital))
                        .collect(Collectors.toList()));

        return hospital;
    }

    public static Hospital toHospitalEntity(HospitalDto hospitalDto, PhotoData thumbnail, PhotoData storeInfoPhoto) {
        if (hospitalDto == null) {
            throw new IllegalArgumentException("HospitalDto is null");
        }
//thumbnail is Nullable

        Hospital hospital = Hospital.builder()
                .additionalServiceTag(hospitalDto.additionalServiceTag())
                .storeName(hospitalDto.hospitalName())
                .storePhone(hospitalDto.hospitalPhone())
                .notice(hospitalDto.notice())
                .websiteLink(hospitalDto.websiteLink())
                .address(AddressDto.toEntity(hospitalDto.address()))
                .storeInfo(hospitalDto.storeInfo())
                .storeInfoPhoto(storeInfoPhoto)
                .thumbnail(thumbnail)
                .storePhotos(new ArrayList<>())
                .businessHours(new ArrayList<>())
                .registrationInfo(RegistrationInfoDto.Request.toEntity(hospitalDto.registrationInfo(), StoreType.HOSPITAL))
                .build();

        hospital.getBusinessHours()
                .addAll(hospitalDto.businessHours()
                        .stream()
                        .map(businessHour -> BusinessHourDto.Request.toEntity(businessHour, hospital))
                        .collect(Collectors.toList()));

        return hospital;
    }
}
