package com.pettoyou.server.domains.scrap.repository.custom;

import com.pettoyou.server.domains.scrap.dto.response.ScrapQueryRespDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pettoyou.server.member.entity.QMember.*;
import static com.pettoyou.server.scrap.entity.QScrap.*;
import static com.pettoyou.server.store.entity.QStore.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScrapCustomRepositoryImpl implements ScrapCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<ScrapQueryRespDto> findScrapListByMemberId(Long memberId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                ScrapQueryRespDto.class,
                                scrap.scrapId,
                                store.thumbnail.photoUrl,
                                store.storeName,
                                store.address
                        )
                )
                .from(scrap)
                .leftJoin(scrap.member, member)
                .leftJoin(scrap.store, store)
                .where(scrap.member.memberId.eq(memberId))
                .fetch();
    }
}
