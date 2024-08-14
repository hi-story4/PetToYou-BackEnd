package com.pettoyou.server.domains.banner.service;

import com.pettoyou.server.domains.banner.dto.request.BannerRegisterRequestDto;
import com.pettoyou.server.domains.banner.dto.response.BannerRegisterResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface BannerService {

    BannerRegisterResponseDto bannerRegister(
            BannerRegisterRequestDto bannerRegisterRequestDto,
            MultipartFile bannerImg);

    BannerRegisterResponseDto bannerModify(
            BannerRegisterRequestDto bannerRegisterRequestDto,
            MultipartFile bannerImg,
            Long bannerId);

    void bannerDelete(Long bannerId);
}
