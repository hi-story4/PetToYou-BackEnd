package com.pettoyou.server.domains.auth;

import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;

public interface OAuthApiClient {
    /***
     *클라이언트의 타입 변환
     */
    OAuthProvider oauthProvider();

    /***
     * AuthorizationCode로 AccessToken 받아오는 메서드
     */
    String requestAccessToken(OAuthLoginParams params);

    /***
     * AccessToken을 이용해서 OAuth 사용자 정보를 받아오는 메서드
     */
    OAuthInfoResponse requestOAuthInfo(String accessToken);
}
