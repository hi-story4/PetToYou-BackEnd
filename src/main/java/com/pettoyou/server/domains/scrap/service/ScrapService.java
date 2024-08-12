package com.pettoyou.server.domains.scrap.service;

import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;
import com.pettoyou.server.domains.scrap.dto.response.ScrapRegistRespDto;

import java.util.List;

public interface ScrapService {

    ScrapRegistRespDto scrapRegist(Long storeId, Long memberId);

    void scrapCancel(Long scrapId, Long memberId);

    List<ScrapQueryRespDto> fetchScrapStore(Long memberId);
}
