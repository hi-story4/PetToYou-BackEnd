package com.pettoyou.server.domains.store.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pettoyou.server.domains.store.util.PointSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {
    @NotNull
    private String zipCode;

    private String addressDetail;

    @NotNull
    private String sido;

    @NotNull
    private String sigungu;

    @NotNull
    private String doro;

    @NotNull
    private String buildingNumber;

    @JsonSerialize(using = PointSerializer.class)
    @NotNull
    @Column(nullable = true, columnDefinition = "POINT SRID 4326")
    private Point point;
    //SRID 4326은 위도 경도 순으로  y, x

    public String generateDefaultAddressFormat() {
        return sido + " " + sigungu + " " + doro;
    }
}