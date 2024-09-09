package com.pettoyou.server.config.jwt.util;

import com.pettoyou.server.domains.member.entity.enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
@Getter
@Slf4j
public class JwtUtil {
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String TOKEN_TYPE = "token_type";
    private static final String TOKEN_TYPE_MEMBER = "MEMBER";
    private static final String TOKEN_TYPE_H_ADMIN = "HOSPITAL_ADMIN";
    private static final String ROLE = "role";
    private static final String BEARER = "Bearer ";

    private final String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION_TIME;
    private final long REFRESH_TOKEN_EXPIRATION_TIME;
    private final UserDetailsService userDetailsService;
    private final UserDetailsService hospitalAdminDetailsService;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time.access_token}") long accessTokenExprTime,
            @Value("${jwt.expiration_time.refresh_token}") long refreshTokenExprTime,
            @Qualifier("principalDetailsServiceImpl") UserDetailsService userDetailsService,
            @Qualifier("hospitalAdminDetailsServiceImpl") UserDetailsService hospitalAdminDetailsService
    ) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_TOKEN_EXPIRATION_TIME = accessTokenExprTime;
        this.REFRESH_TOKEN_EXPIRATION_TIME = refreshTokenExprTime;
        this.userDetailsService = userDetailsService;
        this.hospitalAdminDetailsService = hospitalAdminDetailsService;
    }

    /***
     * @param secretKey : yml 에 저장되어 있는 secret key
     * @return : secret key 를 인코딩 하여 Key 객체로 리턴
     */
    private Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰을 파싱하여 토큰에 들어있는 Claim을 리턴
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰속(claim)에 있는 클라이언트의 role을 리턴
     */
    public List<RoleType> getRolesInToken(String token) {
        // 토큰에서 ROLE 클레임을 List<String>으로 가져옴
        List<String> rolesAsString = extractAllClaims(token).get(ROLE, List.class);

        // List<String>을 List<RoleType>으로 변환
        return rolesAsString.stream()
                .map(RoleType::valueOf)  // 문자열을 RoleType 열거형으로 변환
                .toList();
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰속(claim)에 있는 클라이언트의 email 리턴
     */
    public String getEmailInToken(String token) {
        return extractAllClaims(token).get(EMAIL, String.class);
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰속(claim)에 있는 클라이언트의 username 리턴
     */
    public String getUsernameInToken(String token) {
        return extractAllClaims(token).get(USERNAME, String.class);
    }

    public String getTokenTypeInToken(String token) {
        return extractAllClaims(token).get(TOKEN_TYPE, String.class);
    }


    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰을 이용하여 로그인 된 UPA 객체를 가져옴 -> UPA 객체 안에 유저의 권한들이 담겨 있음
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserDetailsByToken(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private UserDetails loadUserDetailsByToken(String token) {
        String username = getTokenTypeInToken(token).equals(TOKEN_TYPE_MEMBER)
                ? getUsernameInToken(token)
                : getEmailInToken(token);

        return getTokenTypeInToken(token).equals(TOKEN_TYPE_MEMBER)
                ? hospitalAdminDetailsService.loadUserByUsername(username)
                : userDetailsService.loadUserByUsername(username);
    }

    /***
     *
     * @param email : claim 에 넣기 위한 클라이언트의 이메일
     * @param roles : claim에 넣기 위한 클라이언트의 권한들
     * @param tokenType : 액세스 토큰과 리프레시 토큰을 구분짓기 위한 토큰타입
     * @return : 토큰 타입에 맞는 토큰을 생성하여 리턴
     */
    public String createMemberToken(String email, List<RoleType> roles, TokenType tokenType) {
        Claims claims = createClaimsWithEmail(email, roles);

        long expirationTime = getExpirationTime(tokenType);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    /***
     * @param username : claim 에 넣기 위한 클라이언트의 username
     * @param roles : claim에 넣기 위한 클라이언트의 권한들
     * @param tokenType : 액세스 토큰과 리프레시 토큰을 구분짓기 위한 토큰타입
     * @return : 토큰 타입에 맞는 토큰을 생성하여 리턴
     */
    public String createHospitalAdminToken(String username, List<RoleType> roles, TokenType tokenType) {
        Claims claims = createClaimsWithUsername(username, roles);

        long expirationTime = getExpirationTime(tokenType);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims createClaimsWithEmail(String email, List<RoleType> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(EMAIL, email);
        claims.put(TOKEN_TYPE, TOKEN_TYPE_H_ADMIN);
        if (roles != null && !roles.isEmpty()) {
            claims.put(ROLE, roles.stream().map(Enum::name).toList());
        }
        return claims;
    }

    private Claims createClaimsWithUsername(String username, List<RoleType> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(USERNAME, username);
        claims.put(TOKEN_TYPE, TOKEN_TYPE_MEMBER);
        if (roles != null && !roles.isEmpty()) {
            claims.put(ROLE, roles.stream().map(Enum::name).toList());
        }
        return claims;
    }

    private long getExpirationTime(TokenType tokenType) {
        return tokenType == TokenType.ACCESS_TOKEN
                ? ACCESS_TOKEN_EXPIRATION_TIME
                : REFRESH_TOKEN_EXPIRATION_TIME;
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰 문자열 앞의 Bearer 을 제거하고 토큰 문자열만 리턴
     */
    public String resolveToken(String token) {
        if (token == null) return "";
        return token.substring(BEARER.length());
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰의 유효기간이 얼마나 남았는지 리턴
     */
    public Long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }

    public Long getExpiration(TokenType tokenType) {
        return tokenType.equals(TokenType.ACCESS_TOKEN) ? ACCESS_TOKEN_EXPIRATION_TIME : REFRESH_TOKEN_EXPIRATION_TIME;
    }
}
