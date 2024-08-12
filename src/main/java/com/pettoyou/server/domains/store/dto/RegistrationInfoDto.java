package com.pettoyou.server.domains.store.dto;

import com.pettoyou.server.domains.store.entity.RegistrationInfo;
import com.pettoyou.server.domains.store.entity.enums.StoreType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for {@link RegistrationInfo}
 */

public class RegistrationInfoDto{
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        String ceoName;
        @NotNull
        String ceoPhone;
        @NotNull
        @Email
        String ceoEmail;
        @NotNull
        String businessNumber;

        public static RegistrationInfo toEntity (RegistrationInfoDto.Request reg, StoreType stoereType){
            return RegistrationInfo.builder()
                    .ceoName(reg.ceoName)
                    .ceoPhone(reg.ceoPhone)
                    .ceoEmail(reg.ceoEmail)
                    .businessNumber(reg.businessNumber)
                    .storeType(stoereType)
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        String ceoName;
        @NotNull
        String ceoPhone;
        @NotNull
        @Email
        String ceoEmail;
        @NotNull
        String businessNumber;
        @Enumerated
        StoreType storeType;

        public static RegistrationInfoDto.Response toDto (RegistrationInfo reg){
            return RegistrationInfoDto.Response.builder()
                    .ceoName(reg.getCeoName())
                    .ceoPhone(reg.getCeoPhone())
                    .ceoEmail(reg.getCeoEmail())
                    .businessNumber(reg.getBusinessNumber())
                    .storeType(reg.getStoreType())
                    .build();
        }
    }

}

//

