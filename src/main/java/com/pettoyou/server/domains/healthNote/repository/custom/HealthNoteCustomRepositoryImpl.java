package com.pettoyou.server.domains.healthNote.repository.custom;

import com.pettoyou.server.domains.healthNote.dto.response.HealthNoteSimpleInfoDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pettoyou.server.healthNote.entity.QHealthNote.*;
import static com.pettoyou.server.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HealthNoteCustomRepositoryImpl implements HealthNoteCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HealthNoteSimpleInfoDto> findHealthNotesByPetId(Long petId) {
        List<Tuple> results = jpaQueryFactory
                .select(healthNote, store.storeName)
                .from(healthNote)
                .leftJoin(store).on(healthNote.storeId.eq(store.storeId))
                .where(healthNote.petId.eq(petId))
                .orderBy(healthNote.createdAt.asc())
                .fetch();

        return results.stream()
                .map(tuple -> HealthNoteSimpleInfoDto.of(
                        tuple.get(healthNote),
                        tuple.get(store.storeName)
                ))
                .toList();
    }
}
