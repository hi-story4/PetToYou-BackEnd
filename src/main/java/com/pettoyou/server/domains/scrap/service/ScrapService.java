package com.pettoyou.server.domains.scrap.service;

import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;

import java.util.List;

public interface ScrapService {

    void registScrap(Long storeId, Long memberId);

    void cancelScrap(Long scrapId, Long memberId);

    List<ScrapQueryRespDto> fetchScrapStore(Long memberId);
}
