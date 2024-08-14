package com.pettoyou.server.domains.auth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pettoyou.server.domains.auth.OAuthInfoResponse;
import com.pettoyou.server.domains.member.entity.enums.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Builder
    @NoArgsConstructor // Jackson의 json으로의 deserialize를 위해 추가한 기본생성자
    @AllArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
        private String name;
        private String phone_number;
    }

    @Builder
    @NoArgsConstructor // Jackson의 json으로의 deserialize를 위해 추가한 기본생성자
    @AllArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoProfile {
        // 예시로 닉네임이지. 추가로 프사 url 정보도 여기서 받아오면 됨
        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.getNickname();
    }

    @Override
    public String getPhone() {
        if (Objects.isNull(kakaoAccount.getPhone_number())) {
            return "NOT-REGIST";
        }
        return kakaoAccount.getPhone_number().replaceFirst("\\+82\\s?10-", "010-");
    }

    @Override
    public String getName() {
        return kakaoAccount.getName();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
