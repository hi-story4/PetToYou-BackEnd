package com.pettoyou.server.domains.pet.validator;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetValidator {
    private final PetRepository petRepository;

    public void verifyPetAuthorization(
            Long petId,
            Long authMemberId
    ) {
        petRepository.findPetUsingPetIdAndMemberId(petId, authMemberId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.PET_NOT_FOUND)
        );
    }
}
