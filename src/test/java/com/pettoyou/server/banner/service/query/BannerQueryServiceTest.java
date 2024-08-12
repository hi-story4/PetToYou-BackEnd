package com.pettoyou.server.banner.service.query;

import com.pettoyou.server.domains.banner.dto.response.BannerQueryRespDto;
import com.pettoyou.server.domains.banner.entity.Banner;
import com.pettoyou.server.domains.banner.repository.BannerRepository;
import com.pettoyou.server.domains.banner.service.query.BannerQueryServiceImpl;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BannerQueryServiceTest {

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private BannerQueryServiceImpl bannerQueryService;

    /***
     * 배너 조회
     */

    @Test
    void 배너_정상_조회() {
        // given
        List<Banner> banners = createBanners();

        when(bannerRepository.findAll()).thenReturn(banners);

        // when
        List<BannerQueryRespDto> result = bannerQueryService.queryBanners();

        // then
        for (int i = 0; i < banners.size(); i++) {
            assertThat(result.get(i).bannerId()).isEqualTo(banners.get(i).getBannerId());
            assertThat(result.get(i).bannerName()).isEqualTo(banners.get(i).getBannerName());
            assertThat(result.get(i).bannerImgUrl()).isEqualTo(banners.get(i).getBannerImg().getPhotoUrl());
            assertThat(result.get(i).bannerLink()).isEqualTo(banners.get(i).getBannerLink());
        }
    }

    @Test
    void 빈_배너_리스트_정상_처리() {
        // given
        List<Banner> emptyBanners = Collections.emptyList();

        when(bannerRepository.findAll()).thenReturn(emptyBanners);

        // when
        List<BannerQueryRespDto> result = bannerQueryService.queryBanners();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 배너_필드가_null인_경우_정상_처리() {
        // given
        List<Banner> banners = new ArrayList<>();
        banners.add(Banner.builder()
                .bannerId(1L)
                .bannerName(null)
                .bannerImg(null)
                .build());

        when(bannerRepository.findAll()).thenReturn(banners);

        // when
        List<BannerQueryRespDto> result = bannerQueryService.queryBanners();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).bannerName()).isNull();
        assertThat(result.get(0).bannerImgUrl()).isEqualTo("empty");
    }

    private List<Banner> createBanners() {
        List<Banner> list = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            list.add(createBanner(i));
        }
        return list;
    }

    private Banner createBanner(long id) {
        return Banner.builder()
                .bannerId(id)
                .bannerName("name" + id)
                .bannerLink("link" + id)
                .bannerImg(PhotoData.of("bucket" + id, "object" + id, "url" + id))
                .build();
    }
}