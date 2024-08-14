package com.pettoyou.server.domains.member.controller;

import com.pettoyou.server.domains.auth.kakao.KakaoLoginParam;
import com.pettoyou.server.domains.auth.naver.NaverLoginParam;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.constant.entity.AuthTokens;
import com.pettoyou.server.domains.member.dto.response.LoginAndReissueRespDto;
import com.pettoyou.server.domains.member.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class MemberAuthController {
    private final AuthService authService;

    // 로그인 및 강제 회원가입
    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<LoginAndReissueRespDto>> loginKakao(
            @RequestParam String code,
            HttpServletResponse response
    ) {
        AuthTokens authTokens = authService.signIn(KakaoLoginParam.from(code));

        Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        return ApiResponse.createSuccessWithOk(LoginAndReissueRespDto.from(authTokens));
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<ApiResponse<LoginAndReissueRespDto>> loginNaver(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletResponse response
    ) {
        AuthTokens authTokens = authService.signIn(NaverLoginParam.of(code, state));

        Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        return ApiResponse.createSuccessWithOk(LoginAndReissueRespDto.from(authTokens));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<LoginAndReissueRespDto>> reissue(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        log.info("refreshToken : {}", refreshToken);
        AuthTokens authTokens = authService.reissue(refreshToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        return ApiResponse.createSuccessWithOk(LoginAndReissueRespDto.from(authTokens));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ApiResponse.createSuccessWithOk("로그아웃이 완료되었습니다.");
    }
}
