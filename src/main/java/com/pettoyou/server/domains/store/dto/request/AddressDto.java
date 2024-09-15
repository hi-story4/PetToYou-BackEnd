package com.pettoyou.server.domains.store.dto.request;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.store.entity.Address;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * DTO for {@link Address}
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class AddressDto {
    @NotNull(message = "우편번호를 입력해주세요")
    @Pattern(regexp = "^\\d{3}-\\d{2}$")
    private String zipCode;

    @NotNull(message = "주소(시도)를 입력해주세요")
    private String sido;

    @NotNull(message = "주소(시군구)를 입력해주세요")
    private String sigungu;

    private String buildingNumber;

    @NotNull(message = "도로명주소를 입력해주세요")
    private String doro;

    private String addressDetail;

    @NotNull(message = "위도를 입력해주세요")
    private double latitude;

    @NotNull(message = "경도를 입력해주세요")
    private double longitude;

    public static Address toEntity(AddressDto addressDto) {

        if (addressDto.longitude < 124.0 || addressDto.longitude > 134.0) {
            throw new CustomException(CustomResponseStatus.INVALID_LONGITUDE_ERROR);
        }
        if (addressDto.latitude < 34 || addressDto.latitude > 44) {
            throw new CustomException(CustomResponseStatus.INVALID_LATITUDE_ERROR);
        }


        String pointWKT = (String.format("POINT(%s %s)", addressDto.longitude, addressDto.latitude));
        Point point = null;
        log.info(pointWKT);


        try {
            point = (Point) new WKTReader().read(pointWKT);
            point.setSRID(4326);
            log.info(point.toString());
        } catch (ParseException e) {
            throw new CustomException(CustomResponseStatus.POINT_PARSING_ERROR);
        }

        return Address.builder()
                .sido(addressDto.sido)
                .sigungu(addressDto.sigungu)
                .buildingNumber(addressDto.buildingNumber)
                .doro(addressDto.doro)
                .addressDetail(addressDto.addressDetail)
                .zipCode(addressDto.zipCode)
                .point(point)
                .build();

    }
}