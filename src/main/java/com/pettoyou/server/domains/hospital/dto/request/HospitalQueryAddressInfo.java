package com.pettoyou.server.domains.hospital.dto.request;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;

public record HospitalQueryAddressInfo(
        double latitude,
        double longitude
) {
    public HospitalQueryAddressInfo{
        if (latitude < 34 || latitude > 44) {
            throw new CustomException(CustomResponseStatus.INVALID_LATITUDE_ERROR);

        }
        if (longitude < 124 || longitude > 134) {
            throw new CustomException(CustomResponseStatus.INVALID_LONGITUDE_ERROR);
        }

    }

    public String toPointString() {
        return String.format("POINT (%f %f)", this.latitude, this.longitude);
    }

}
