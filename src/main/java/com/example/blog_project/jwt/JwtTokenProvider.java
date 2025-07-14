package com.example.blog_project.jwt;

import com.example.blog_project.CustomUserDetails;
import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider
{

    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String secretKeyString;

    private Key key;

    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    private long tokenValidityInMilliseconds;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    // 스프링 객체 생성 후 초기화 시 실행
    @PostConstruct
    public void init() {
        String base64EncodeSecretKey =
                Base64.getEncoder().encodeToString(secretKeyString.getBytes());
        this.key = Keys.hmacShaKeyFor(base64EncodeSecretKey.getBytes());
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000L;
    }

    // JWT를 생성 후, 반환
    public String generateToken(String username, String role) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds); //토큰 생성 시간

        return Jwts.builder()
                .setSubject(username) //주체
                .claim("auth", role) //권한
                .setIssuedAt(new Date(now)) //발급 시간
                .setExpiration(validity) //토큰 만료 시간
                .signWith(this.key, SignatureAlgorithm.HS256) // 서명에 사용할 키와, 방식
                .compact();
    }

    // 토큰 유효 검증
    public boolean validateToken(String token)
    {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.", e);
        }
        return false;

    }

    // JWT를 스프링 시큐리티의 Authentication으로 변환
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails,token,authorities);
    }
}
