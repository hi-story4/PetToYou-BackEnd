package com.pettoyou.server.domains.auth;

import com.pettoyou.server.config.jwt.util.JwtUtil;
import com.pettoyou.server.config.jwt.util.TokenType;
import com.pettoyou.server.config.jwt.util.TokenUserType;
import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.domains.member.entity.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private final JwtUtil jwtUtil;

    public AuthTokens generate(String email, List<RoleType> roles) {
        String accessToken = jwtUtil.createToken(email, roles, TokenType.ACCESS_TOKEN, TokenUserType.MEMBER_TOKEN);
        String refreshToken = jwtUtil.createToken(email, null, TokenType.REFRESH_TOKEN, TokenUserType.MEMBER_TOKEN);

        return AuthTokens.of(accessToken, refreshToken, jwtUtil.getExpiration(TokenType.ACCESS_TOKEN));
    }

    public AuthTokens generate(String email, List<RoleType> roles, String refreshToken) {
        String accessToken = jwtUtil.createToken(email, roles, TokenType.ACCESS_TOKEN, TokenUserType.MEMBER_TOKEN);
        return AuthTokens.of(accessToken, refreshToken, jwtUtil.getExpiration(TokenType.ACCESS_TOKEN));
    }

    public AuthTokens generateAdminToken(String username, List<RoleType> roles, String refreshToken) {
        String accessToken = jwtUtil.createToken(username, roles, TokenType.ACCESS_TOKEN, TokenUserType.HOSPITAL_ADMIN_TOKEN);
        return AuthTokens.of(accessToken, refreshToken, jwtUtil.getExpiration(TokenType.ACCESS_TOKEN));
    }
}
