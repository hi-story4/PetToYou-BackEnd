package com.pettoyou.server.domains.pet.repository.custom;

import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;

import java.util.List;
import java.util.Optional;

public interface PetCustomRepository {
    List<PetDetailInfoRespDto> findAllPetsByMemberId(Long memberId);

    Optional<Pet> findPetUsingPetIdAndMemberId(Long petId, Long memberId);

    String getPetNameByPetId(Long petId);
}
