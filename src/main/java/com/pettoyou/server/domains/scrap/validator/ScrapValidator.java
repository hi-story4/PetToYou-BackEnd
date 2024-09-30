package com.pettoyou.server.domains.scrap.validator;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.scrap.entity.Scrap;
import com.pettoyou.server.domains.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapValidator {
    private final ScrapRepository scrapRepository;

    public void validateScrapExists(
            Long storeId,
            Long authMemberId
    ) {
        if (scrapRepository.existsByMemberMemberIdAndStoreStoreId(authMemberId, storeId)) {
            throw new CustomException(CustomResponseStatus.SCRAP_ALREADY_EXIST);
        }
    }

    public void validateScrapOwnership(
            Scrap scrap,
            Long authMemberId
    ) {
        scrap.validateOwnerAuthorization(authMemberId);
    }
}
