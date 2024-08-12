package com.pettoyou.server.domains.auth;

import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
