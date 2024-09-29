package com.pettoyou.server.domains.pet.service.query;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.pet.dto.response.PetDetailInfoRespDto;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetQueryServiceImpl implements PetQueryService {
    private final PetRepository petRepository;

    @Override
    public List<PetDetailInfoRespDto> fetchClientPets(Long userId) {
        return petRepository.findAllPetsByMemberId(userId);
    }

    @Override
    public PetDetailInfoRespDto fetchPetDetailInfo(
            Long petId,
            Long authMemberId
    ) {
        Pet pet = findPetById(petId);
        pet.validateOwnerAuthorization(authMemberId);

        return PetDetailInfoRespDto.from(pet);
    }

    private Pet findPetById(Long petId) {
        return petRepository.findById(petId).orElseThrow(
                () -> new CustomException(CustomResponseStatus.PET_NOT_FOUND)
        );
    }
}
