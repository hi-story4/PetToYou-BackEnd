package com.pettoyou.server.domains.auth;

import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;

public interface OAuthInfoResponse {

    String getId();

    String getEmail();

    String getNickname();

    String getPhone();

    String getName();

    OAuthProvider getOAuthProvider();
}
