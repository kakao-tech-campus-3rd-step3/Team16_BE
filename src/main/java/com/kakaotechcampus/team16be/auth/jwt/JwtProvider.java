package com.kakaotechcampus.team16be.auth.jwt;

import com.kakaotechcampus.team16be.auth.exception.JwtErrorCode;
import com.kakaotechcampus.team16be.auth.exception.JwtException;
import com.kakaotechcampus.team16be.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private static final int ACCESS_TOKEN_EXPIRATION = 60 * 60 * 24 * 365;//테스트 편의를 위해 1년으로 설정

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // secretKey 초기화
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 생성
     * @param user 토큰에 담을 User 정보
     * @return JWT 문자열
     */
    public String createToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION * 1000L);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("kakaoId", user.getKakaoId())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 파싱 및 검증
     * @param token 클라이언트가 전달한 JWT
     * @return Claims payload
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new JwtException(JwtErrorCode.WRONG_HEADER_TOKEN);
        }
    }

    /**
     * JWT에서 userId 추출
     */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new JwtException(JwtErrorCode.WRONG_HEADER_TOKEN);
        }
    }

    /**
     * JWT에서 kakaoId 추출
     */
    public String getKakaoId(String token) {
        Claims claims = parseToken(token);
        Object kakaoId = claims.get("kakaoId");
        return kakaoId != null ? kakaoId.toString() : null;
    }
}
