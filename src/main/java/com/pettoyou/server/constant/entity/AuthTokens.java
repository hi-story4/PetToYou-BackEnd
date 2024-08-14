package com.pettoyou.server.constant.entity;

import lombok.Builder;

@Builder
public record AuthTokens (
        String accessToken,
        String refreshToken,
        Long exprTime
) {

    public static AuthTokens of(String accessToken, String refreshToken, Long exprTime) {
        return AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .exprTime(exprTime)
                .build();
    }
}
