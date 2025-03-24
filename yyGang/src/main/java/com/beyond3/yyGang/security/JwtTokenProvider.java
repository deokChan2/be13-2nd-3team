package com.beyond3.yyGang.security;

import com.beyond3.yyGang.config.RedisConfig;
import com.beyond3.yyGang.user.domain.Role_name;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component  // 빈으로 등록할 예정
public class JwtTokenProvider {

    private final SecretKey key;  // JWT 서명에 사용될 비밀 키
    //    private final UserDetailsService userDetailsService;
//    private final long ACCESS_TOKEN_EXP = 1000L * 60L * 15L; // 15분
//    private final long REFRESH_TOKEN_EXP = 1000L * 60L * 60L * 15L;    // refresh 토큰 만료 기간
    private final long ACCESS_TOKEN_EXP = 1000L * 60L;
    private final long REFRESH_TOKEN_EXP = 1000L * 60L * 5L;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    // JwtTokenProvider 생성자
    public JwtTokenProvider(
            @Value("${jwt.secret.key}") String secretKey,
            UserDetailsService userDetailsService,
            RedisTemplate<String, String> redisTemplate) {

        // Secret Key를 Base64로 디코딩 -> 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // HMAC-SHA 알고리즘을 사용할 수 있는 서명용 비밀 키로 변환, this.key에 저장
        // BASE64 디코딩을 통해 더 안전하게 처리
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.userDetailsService = userDetailsService;

        this.redisTemplate = redisTemplate;
    }

    // Access Token 생성 메소드
    public String createAccessToken(String username, Role_name role) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("username", username);
        claims.put("role", role);

        return createToken(claims, ACCESS_TOKEN_EXP);
    }

    public String createRefreshToken(String username) {
        Map<String , Object > claims = Map.of("username" , username);
        String refreshToken = createToken(claims, REFRESH_TOKEN_EXP);

        // 생성한 Refresh Token은 Redis 서버에 저장
        redisTemplate.opsForValue().set("refreshToken", refreshToken, REFRESH_TOKEN_EXP, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    public String createToken(Map<String , Object > claims, long tokenExp) {
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims)           // 사용자 권한을 "auths"라는 클레임으로 추가
                .id(Long.toString(System.nanoTime()))   // 현재 시간을 기준으로 유니크한 jti(JWT ID) 생성
                .issuedAt(new Date())   // 발급 시간 설정
                .expiration(new Date(System.currentTimeMillis() + tokenExp))  // Key 만료 기간 설정
                .signWith(key)
                .compact();
    }


    // 토큰 정보 검증 메서드 ->> 남겨두자 이건
    public boolean validateToken(String token) {
        try{
            Jwts.parser()      // jwt parser 객체 생성 - JWT 토큰 분석, 안에 포함된 데이터 추출
                    .setSigningKey(key)   // 서명 검증에 사용할 비밀 key 설정 -> JWT 토큰 생성 시 사용한 key와 일치해야 함
                    .build()
                    .parseClaimsJws(token);  // jwt 토큰을 parsing 하여 클레임을 추출
            return true;  // 토큰이 유효할 경우 true 반환
        } catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT token", e);  // 유효하지 않은 토큰
        } catch (ExpiredJwtException e){
            log.info("Expired JWT token", e);   // 만료된 토큰
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token", e);  // 지원하지 않는 토큰
        } catch (IllegalArgumentException e){
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    // 서버에 전달한 토큰 추출 메소드
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 토큰을 복호화해서 토큰에 들어있는 정보를 꺼내는 메서드
    // 꺼낸 정보를 바탕으로 SecurityHolder에 들어갈 Authentication 객체를 생성해줌
    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자 이름 정보 가져오기
        String username = parseClaims(token).get("username", String.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰에서 클레임(Claim)을 추출하는 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts
                    .parser()   // JWT Parser를 생성
                    .verifyWith(key) // 서명 검증에 사용할 비밀 키 설정
                    .build()
                    .parseSignedClaims(token) // JWT 토큰을 파싱해 claim 생성
                    .getPayload();     // claim 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims();   // 만료된 토큰일 경우 예외에서 claim을 반환
        }
    }

    public boolean hasRole(String token) {
        return parseClaims(token).get("role") != null;
    }

}