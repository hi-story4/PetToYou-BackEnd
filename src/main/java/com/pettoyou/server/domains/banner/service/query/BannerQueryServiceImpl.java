package com.pettoyou.server.domains.banner.service.query;

import com.pettoyou.server.domains.banner.dto.response.BannerQueryRespDto;
import com.pettoyou.server.domains.banner.entity.Banner;
import com.pettoyou.server.domains.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerQueryServiceImpl implements BannerQueryService {
    private final BannerRepository bannerRepository;

    @Override
    public List<BannerQueryRespDto> queryBanners() {
        List<Banner> banners = bannerRepository.findAll();
        return banners.stream().map(BannerQueryRespDto::from).toList();
    }
}
