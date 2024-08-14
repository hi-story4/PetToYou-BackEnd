package com.pettoyou.server.domains.auth.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pettoyou.server.domains.auth.OAuthInfoResponse;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor // Jackson의 json으로의 deserialize를 위해 추가한 기본생성자
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {
    @JsonProperty("response")
    private Response response;

    @Getter
    @Builder
    @NoArgsConstructor // Jackson의 json으로의 deserialize를 위해 추가한 기본생성자
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private String id;
        private String email;
        private String nickname;
        private String name;
        private String mobile;
    }

    @Override
    public String getId() {
        return response.getId();
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }

    @Override
    public String getNickname() {
        return response.getNickname();
    }

    @Override
    public String getPhone() {
        return response.getMobile();
    }

    @Override
    public String getName() {
        return response.getName();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
