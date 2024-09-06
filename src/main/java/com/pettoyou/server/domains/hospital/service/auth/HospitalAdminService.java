package com.pettoyou.server.domains.hospital.service.auth;

import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin.HospitalAdminSignUpReqDto;
import com.pettoyou.server.domains.hospital.dto.request.hospitalAdmin.HospitalAdminSignInReqDto;

public interface HospitalAdminService {
    void singUp(HospitalAdminSignUpReqDto signUpReqDto);
    AuthTokens signIn(HospitalAdminSignInReqDto signInReqDto);
}
