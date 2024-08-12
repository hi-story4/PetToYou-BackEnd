package com.pettoyou.server.domains.store.dto;

import com.pettoyou.server.domains.store.entity.BusinessHour;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.entity.enums.StoreType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

/**
 * DTO for {@link BusinessHour}
 */
public class BusinessHourDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @Min(1) @Max(7)
        Integer dayOfWeek;
        //월화수목금토일 1~7
        Time startTime;
        Time endTime;
        Time breakStartTime;
        Time breakEndTime;
        boolean openSt;

        public static BusinessHour toEntity(BusinessHourDto.Request req, Store store) {

            return BusinessHour.builder()
                    .store(store)
                    .dayOfWeek(req.getDayOfWeek())
                    .openSt(req.isOpenSt())
                    .startTime(req.getStartTime())
                    .endTime(req.getEndTime())
                    .breakStartTime(req.getBreakStartTime())
                    .breakEndTime(req.getBreakEndTime()).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;

        @NotNull
        Long businessHourId;
        StoreType storeType;
        @Positive
        Integer dayOfWeek;
        Time startTime;
        Time endTime;
        Time breakStartTime;
        Time breakEndTime;
        boolean openSt;
        Long storeId;


        public static BusinessHourDto.Response toDto(BusinessHour businessHour) {

            return BusinessHourDto.Response.builder()
                    .createdAt(businessHour.getCreatedAt())
                    .modifiedAt(businessHour.getModifiedAt())
                    .businessHourId(businessHour.getBusinessHourId())
                    .dayOfWeek(businessHour.getDayOfWeek())
                    .startTime(businessHour.getStartTime())
                    .endTime(businessHour.getEndTime())
                    .breakStartTime(businessHour.getBreakStartTime())
                    .breakEndTime(businessHour.getBreakEndTime())
                    .openSt(businessHour.isOpenSt())
                    .storeId(businessHour.getStore().getStoreId())
                    .build();
        }
    }
}