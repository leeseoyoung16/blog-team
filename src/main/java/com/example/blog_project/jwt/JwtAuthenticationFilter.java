package com.example.blog_project.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // 모든 HTTP 요청에 대해 필터링 로직을 수행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Swagger 및 인증 관련 경로는 필터 적용 제외
        if (uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/webjars")
                || uri.equals("/swagger-ui.html")
                || uri.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = resolveToken(request);

        if(jwt != null && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            log.info("인증 정보 설정 시도: Principal={}, Authorities={}",
                    authentication.getPrincipal(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("유효하지 않거나 토큰 없음: JWT={}, 유효성={}", jwt, (jwt != null ? jwtTokenProvider.validateToken(jwt) : "null"));
        }
        filterChain.doFilter(request, response);
    }

    // HTTP 요청 헤더에서 "Bearer " 접두사를 제거하고 실제 JWT 토큰 문자열만 추출
    private String resolveToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}