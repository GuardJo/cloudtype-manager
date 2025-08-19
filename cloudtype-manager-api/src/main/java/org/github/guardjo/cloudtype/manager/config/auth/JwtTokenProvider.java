package org.github.guardjo.cloudtype.manager.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.properties.JwtProperties;
import org.github.guardjo.cloudtype.manager.service.RefreshTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public JwtTokenProvider(JwtProperties jwtProperties, UserDetailsService userDetailsService, RefreshTokenService refreshTokenService) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * 인증 객체를 기반으로 JWT access-token을 생성 및 반환한다
     *
     * @param authentication 인증 객체
     * @return JWT 토큰 (access-token)
     */
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtProperties.getAccessTokenExpirationMillis());
    }

    /**
     * 인증 객체를 기반으로 JWT refresh-token을 생성 및 반환한다
     *
     * @param authentication 인증 객체
     * @return JWT 토큰 (refresh-token)
     */
    public String generateRefreshToken(Authentication authentication) {
        String token = generateToken(authentication, jwtProperties.getRefreshTokenExpirationMillis());

        refreshTokenService.saveNewToken(token, authentication.getName());

        return token;
    }

    /**
     * 주어진 token에 대한 유휴성 검증 작업을 수행한다.
     *
     * @param token JWT 토큰
     * @return 유효 여부 반환
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * 토큰 데이터를 기반으로 토큰에 해당하는 인증객체를 반환한다.
     *
     * @param token JWT 토큰
     * @return 인증 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String generateToken(Authentication authentication, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
