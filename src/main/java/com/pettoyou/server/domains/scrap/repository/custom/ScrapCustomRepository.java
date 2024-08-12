package com.pettoyou.server.domains.scrap.repository.custom;

import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;

import java.util.List;

public interface ScrapCustomRepository {
    List<ScrapQueryRespDto> findScrapListByMemberId(Long memberId);
}
