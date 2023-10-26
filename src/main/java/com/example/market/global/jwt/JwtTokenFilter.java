package com.example.market.global.jwt;

import com.example.market.domain.user.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더 존재하는지 확인
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 존재한다면 헤더가 Bearer 로 시작하며, 뒤의 JWT가 유효한지 확인
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtil.validate(token)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                String username = jwtTokenUtil
                        .parseClaims(token)
                        .getSubject();
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .username(username)
                                .build(),
                        token, new ArrayList<>()
                );
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
                log.info("set security context with jwt");
            } else {
                log.warn("jwt validation failed");
            }
        }
        filterChain.doFilter(request, response);
    }
}
