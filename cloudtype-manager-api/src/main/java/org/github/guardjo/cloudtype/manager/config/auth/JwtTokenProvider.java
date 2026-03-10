package org.github.guardjo.cloudtype.manager.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.properties.JwtProperties;
import org.github.guardjo.cloudtype.manager.model.domain.AccessBlackHash;
import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;
import org.github.guardjo.cloudtype.manager.repository.AccessBlackHashRepository;
import org.github.guardjo.cloudtype.manager.repository.RefreshTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserInfoEntityRepository userInfoRepository;
    private final RefreshTokenEntityRepository refreshTokenRepository;
    private final AccessBlackHashRepository accessBlackHashRepository;

    public JwtTokenProvider(JwtProperties jwtProperties, UserDetailsService userDetailsService, UserInfoEntityRepository userInfoEntityRepository, RefreshTokenEntityRepository refreshTokenEntityRepository, AccessBlackHashRepository accessBlackHashRepository) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.userDetailsService = userDetailsService;
        this.userInfoRepository = userInfoEntityRepository;
        this.refreshTokenRepository = refreshTokenEntityRepository;
        this.accessBlackHashRepository = accessBlackHashRepository;
    }

    /**
     * 인증 객체를 기반으로 JWT token을 생성 및 반환한다
     * <hr/>
     * <i>이 때 refresh_token의 경우, DB에 적재</i>
     *
     * @param authentication 인증 객체
     * @return JWT 토큰 (access-token and refresh-token)
     */
    @Transactional
    protected AuthTokenInfo generateAuthTokenInfo(Authentication authentication) {
        String accessToken = generateToken(authentication.getName(), jwtProperties.getAccessTokenExpirationMillis(), jwtProperties.getAccessAudience());
        String refreshToken = generateToken(authentication.getName(), jwtProperties.getRefreshTokenExpirationMillis(), jwtProperties.getRefreshAudience());
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

        String accessToken = generateToken(username, jwtProperties.getAccessTokenExpirationMillis(), jwtProperties.getAccessAudience());
        String newRefreshToken = generateToken(username, jwtProperties.getRefreshTokenExpirationMillis(), jwtProperties.getRefreshAudience());

        refreshTokenEntity.setToken(newRefreshToken);

        return new AuthTokenInfo(accessToken, newRefreshToken);
    }

    /**
     * 토큰 데이터를 기반으로 토큰에 해당하는 인증객체를 반환한다.
     *
     * @param token JWT 토큰
     * @return 인증 객체
     * @throws AuthenticationException 복호화된 JWT 토큰 내 데이터 인가 절차 간 문제 발생
     */
    public Authentication getAuthentication(String token) throws AuthenticationException {
        checkAlreadyLogout(token);

        Claims claims = getClaims(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 인자로 들어온 access_token, refresh_token에 대해 인가 제외 처리
     * <hr/>
     * <i>로그아웃 처리</i>
     *
     * @param authTokenInfo 인증토큰 정보
     * @param username      회원 식별 아이디
     */
    @Transactional
    public void invalidateAuthToken(AuthTokenInfo authTokenInfo, String username) {
        invalidateAccessToken(authTokenInfo.accessToken(), username);
        invalidateRefreshToken(authTokenInfo.refreshToken(), username);
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

    /*
    access-token 내 claim 추출
     */
    private Claims getClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            if (e instanceof SecurityException || e instanceof MalformedJwtException) {
                log.error("Invalid JWT Token", e);
            } else if (e instanceof ExpiredJwtException) {
                log.error("Expired JWT Token", e);
            } else if (e instanceof UnsupportedJwtException) {
                log.error("Unsupported JWT Token", e);
            }
        }

        if (Objects.isNull(claims)) {
            log.error("JWT claims string is empty.");
            throw new BadCredentialsException("JWT claims string is empty.");
        }

        if (StringUtils.isEmpty(claims.getAudience()) || !claims.getAudience().equals(jwtProperties.getAccessAudience())) {
            log.error("Invalid JWT audience, aud = {}, token = {}", claims.getAudience(), token);
            throw new InsufficientAuthenticationException("Invalid JWT audience");
        }

        return claims;
    }

    /*
    이미 로그아웃 처리된 token인지 여부 검증
     */
    private void checkAlreadyLogout(String token) {
        if (accessBlackHashRepository.existsById(token)) {
            log.error("Access token is already logout, token = {}", token);
            throw new BadCredentialsException("Access token is already logout");
        }
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

    private String generateToken(String username, long expirationMillis, String audienceType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setAudience(audienceType)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /* 로그아웃된 access_token black 처리 (유효 기간 동안) */
    private void invalidateAccessToken(String accessToken, String username) {
        Claims claims = getClaims(accessToken);
        String claimsSubject = claims.getSubject();

        if (!claimsSubject.equals(username)) {
            log.error("Do not equals token subject and username, subject = {}, username = {}", claimsSubject, username);
            throw new AccessDeniedException("Do not equals token subject and username");
        }

        AccessBlackHash accessBlackHash = AccessBlackHash.builder()
                .token(accessToken)
                .ttl((int) (jwtProperties.getAccessTokenExpirationMillis() / 1_000L))
                .build();

        accessBlackHashRepository.save(accessBlackHash);
        log.debug("Access token is invalidated, token = {}, ttl = {}", accessBlackHash.getToken(), accessBlackHash.getTtl());
    }

    /* 로그아웃된 refresh_token 제거 처리 */
    private void invalidateRefreshToken(String refreshToken, String username) {
        int deletedRows = refreshTokenRepository.deleteAllByToken(refreshToken, username);

        log.debug("Refresh token is invalidated, deletedRows = {}", deletedRows);
    }
}
