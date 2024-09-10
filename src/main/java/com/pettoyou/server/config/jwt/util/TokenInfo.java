package com.pettoyou.server.config.jwt.util;

import com.pettoyou.server.domains.auth.enums.TokenUserType;

public record TokenInfo(
        String infoInClaim,
        TokenUserType tokenUserType
) {
    public static TokenInfo of(String infoInClaim, TokenUserType tokenUserType) {
        return new TokenInfo(infoInClaim, tokenUserType);
    }
}
