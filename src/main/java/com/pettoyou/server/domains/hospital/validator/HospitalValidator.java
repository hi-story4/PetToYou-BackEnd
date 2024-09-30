package com.pettoyou.server.domains.hospital.validator;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.repository.hospital.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalValidator {
    private final HospitalRepository hospitalRepository;

    public void checkHospitalExist(
            Long hospitalId
    ) {
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new CustomException(CustomResponseStatus.HOSPITAL_NOT_FOUND);
        }
    }
}
