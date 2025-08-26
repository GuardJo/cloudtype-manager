package org.github.guardjo.cloudtype.manager.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.properties.JwtProperties;
import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;
import org.github.guardjo.cloudtype.manager.repository.RefreshTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserInfoEntityRepository userInfoRepository;
    private final RefreshTokenEntityRepository refreshTokenRepository;

    public JwtTokenProvider(JwtProperties jwtProperties, UserDetailsService userDetailsService, UserInfoEntityRepository userInfoEntityRepository, RefreshTokenEntityRepository refreshTokenEntityRepository) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.userDetailsService = userDetailsService;
        this.userInfoRepository = userInfoEntityRepository;
        this.refreshTokenRepository = refreshTokenEntityRepository;
    }

    /**
     * 인증 객체를 기반으로 JWT token을 생성 및 반환한다
     * <hr/>
     * <i>이 때 refresh_token의 경우, DB에 적재</i>
     *
     * @param authentication 인증 객체
     * @return JWT 토큰 (access-token & refresh-token)
     */
    @Transactional
    protected AuthTokenInfo generateAuthTokenInfo(Authentication authentication) {
        String accessToken = generateToken(authentication.getName(), jwtProperties.getAccessTokenExpirationMillis());
        String refreshToken = generateToken(authentication.getName(), jwtProperties.getRefreshTokenExpirationMillis());
        saveNewToken(refreshToken, authentication.getName());

        return new AuthTokenInfo(accessToken, refreshToken);
    }

    /**
     * refresh_token을 기반으로 JWT token을 생성 및 반환한다
     * <hr/>
     * <i>이 때 refresh_token의 경우, DB에 적재</i>
     *
     * @param refreshToken refresh_token
     * @param username     사용자 식별키
     * @return JWT 토큰 (access-token & refresh-token)
     */
    @Transactional
    public AuthTokenInfo generateAuthTokenInfo(String refreshToken, String username) {
        if (!validateToken(refreshToken)) {
            log.error("Invalid JWT refresh-token, username = {}, token = {}", username, refreshToken);
            throw new MalformedJwtException("Invalid JWT refresh-token");
        }

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("The token information doesn't exist on the server., username = {}, token = {}", username, refreshToken);
                    return new MalformedJwtException("The token information doesn't exist on the server.");
                });

        if (!refreshTokenEntity.getUserInfo().getUsername().equals(username)) {
            log.warn("Incorrect refresh-token from user_info, username = {}", username);
            throw new MalformedJwtException("User and token don't match");
        }

        String accessToken = generateToken(username, jwtProperties.getAccessTokenExpirationMillis());
        String newRefreshToken = generateToken(username, jwtProperties.getRefreshTokenExpirationMillis());

        refreshTokenEntity.setToken(newRefreshToken);

        return new AuthTokenInfo(accessToken, newRefreshToken);
    }

    /**
     * 주어진 token에 대한 유휴성 검증 작업을 수행한다.
     *
     * @param token JWT 토큰
     * @return 유효 여부 반환
     */
    protected boolean validateToken(String token) {
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

    private void saveNewToken(String token, String username) {
        log.debug("Creating refresh-token, username = {}", username);

        UserInfoEntity userInfo = userInfoRepository.findById(username)
                .orElseThrow(() -> {
                    log.error("Not Found user_info, username = {}", username);
                    return new EntityNotFoundException(String.format("Not found user_info, username = %s", username));
                });

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(token)
                .userInfo(userInfo)
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Saved refresh-token, username = {}", username);
    }

    private String generateToken(String username, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
