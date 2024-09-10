package com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin;

public record HospitalAdminSignUpReqDto(
        String username,
        String password,
        Long hospitalId
) {
}
