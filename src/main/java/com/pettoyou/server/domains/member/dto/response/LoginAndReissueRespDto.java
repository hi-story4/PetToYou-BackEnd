package com.pettoyou.server.domains.member.dto.response;

import com.pettoyou.server.constant.entity.AuthTokens;
import lombok.Builder;

@Builder
public record LoginAndReissueRespDto(
    String accessToken,
    Long exprTime
) {
    public static LoginAndReissueRespDto from(AuthTokens authTokens) {
        return LoginAndReissueRespDto.builder()
                .accessToken(authTokens.accessToken())
                .exprTime(authTokens.exprTime())
                .build();
    }
}
