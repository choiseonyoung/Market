package com.example.market.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;

@Slf4j
@Component
public class JwtTokenUtil {
    private final Key signingKey;
    // 가지고 있는 Key 정보를 바탕으로 JWT 정보 해석
    private final JwtParser jwtParser;

    public JwtTokenUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }

    // JWT가 유효한지 확인
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("invalid jwt");
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 발급
    public String generateToken(UserDetails userDetails) {
        // JWT에 담길 정보
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(600)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact(); // 실제 Token을 문자열로 만들어 반환
    }



}
