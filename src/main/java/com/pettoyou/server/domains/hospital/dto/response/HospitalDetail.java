package com.pettoyou.server.domains.hospital.dto.response;

import com.pettoyou.server.domains.hospital.dto.HospitalTagDto;
import com.pettoyou.server.domains.hospital.entity.Hospital;
import com.pettoyou.server.domains.hospital.entity.HospitalTag;
import com.pettoyou.server.domains.store.dto.RegistrationInfoDto;
import com.pettoyou.server.domains.store.entity.Address;
import com.pettoyou.server.domains.store.entity.enums.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
public record HospitalDetail(
        @NotNull Long hospitalId,
        @NotNull String hospitalName,
        String storePhone,
        String thumbnailUrl,
        String notice,
        String websiteLink,
        String additionalServiceTag,
        String storeInfo,
        String storeInfoPhoto,
        Address address,
        List<Times> businessHours,
        RegistrationInfoDto.Response registrationInfo,
        HospitalTagDto hospitalTags,
        SubscriptionStatus subscriptionStatus
) {
    public static HospitalDetail from(Hospital hospital, List<HospitalTag> tagList) {
        List<Times> businessHours = Optional.ofNullable(hospital.getBusinessHours())
                .orElse(Collections.emptyList())
                .stream()
                .map(Times::of).toList();

        return HospitalDetail.builder()
                .hospitalId(hospital.getStoreId())
                .hospitalName(hospital.getStoreName())
                .thumbnailUrl(hospital.getThumbnail() == null ? "default_url" : hospital.getThumbnail().getPhotoUrl())
                .storePhone(hospital.getStorePhone())
                .notice(hospital.getNotice())
                .websiteLink(hospital.getWebsiteLink())
                .additionalServiceTag(hospital.getAdditionalServiceTag())
                .storeInfo(hospital.getStoreInfo())
                .storeInfoPhoto(hospital.getStoreInfoPhoto() == null ? null : hospital.getStoreInfoPhoto().getPhotoUrl())
                .address(hospital.getAddress())
                .businessHours(businessHours)
                .registrationInfo(hospital.getRegistrationInfo() == null ? null :RegistrationInfoDto.Response.toDto(hospital.getRegistrationInfo()))
                .hospitalTags(HospitalTagDto.toDto(tagList))
                .subscriptionStatus(hospital.getSubscriptionStatus())
                .build();
    }


}
