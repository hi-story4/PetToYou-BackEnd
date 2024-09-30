package com.pettoyou.server.domains.healthNote.validator;

import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import com.pettoyou.server.domains.healthNote.repository.HealthNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthNoteValidator {
    private final HealthNoteRepository healthNoteRepository;

    public void verifyHealthNoteAuthorization(
            HealthNote healthNote,
            Long authMemberId
    ) {
        healthNote.validateMemberAuthorization(authMemberId);
    }
}
