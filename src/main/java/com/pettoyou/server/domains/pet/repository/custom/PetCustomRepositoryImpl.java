package com.pettoyou.server.domains.pet.repository.custom;

import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.pettoyou.server.member.entity.QMember.*;
import static com.pettoyou.server.pet.entity.QPet.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PetCustomRepositoryImpl implements PetCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PetDetailInfoRespDto> findAllPetsByMemberId(Long memberId) {
        List<Pet> allPets = jpaQueryFactory
                .selectFrom(pet)
                .where(pet.member.memberId.eq(memberId))
                .orderBy(pet.petId.asc())
                .fetch();

        return allPets.stream().map(PetDetailInfoRespDto::from).toList();
    }

    @Override
    public Optional<Pet> findPetUsingPetIdAndMemberId(Long petId, Long memberId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(pet)
                .where(pet.petId.eq(petId).and(pet.member.memberId.eq(memberId)))
                .leftJoin(pet.member, member).fetchJoin()
                .fetchOne());
    }

    @Override
    public String getPetNameByPetId(Long petId) {
        return jpaQueryFactory
                .select(pet.petName)
                .from(pet)
                .where(pet.petId.eq(petId))
                .fetchOne();
    }
}
