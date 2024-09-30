package com.pettoyou.server.domains.pet.validator;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetValidator {
    private final PetRepository petRepository;

    public void validatePetExistsAndOwner(
            Long petId,
            Long authMemberId
    ) {
        if (!petRepository.existsByPetIdAndMember_MemberId(petId, authMemberId)) {
            throw new CustomException(CustomResponseStatus.PET_NOT_FOUND);
        }
    }

    public void validatePetOwnership(
            Pet pet,
            Long authMemberId
    ) {
        pet.validateOwnerAuthorization(authMemberId);
    }
}
