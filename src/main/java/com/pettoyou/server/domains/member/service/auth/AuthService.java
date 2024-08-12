package com.pettoyou.server.domains.member.service.auth;

import com.pettoyou.server.domains.auth.OAuthLoginParams;
import com.pettoyou.server.constant.entity.AuthTokens;

public interface AuthService {
    AuthTokens signIn(OAuthLoginParams param);
    AuthTokens reissue(String refreshToken);
    void logout(String accessToken);
}
