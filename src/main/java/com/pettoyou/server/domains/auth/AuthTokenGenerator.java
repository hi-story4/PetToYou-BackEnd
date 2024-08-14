package com.pettoyou.server.domains.auth;

import com.pettoyou.server.config.jwt.util.JwtUtil;
import com.pettoyou.server.config.jwt.util.TokenType;
import com.pettoyou.server.constant.entity.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private final JwtUtil jwtUtil;

    public AuthTokens generate(String email) {
        String accessToken = jwtUtil.createToken(email, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createToken(email, TokenType.REFRESH_TOKEN);

        return AuthTokens.of(accessToken, refreshToken, jwtUtil.getExpiration(TokenType.ACCESS_TOKEN));
    }

    public AuthTokens generate(String email, String refreshToken) {
        String accessToken = jwtUtil.createToken(email, TokenType.ACCESS_TOKEN);

        return AuthTokens.of(accessToken, refreshToken, jwtUtil.getExpiration(TokenType.ACCESS_TOKEN));
    }
}
