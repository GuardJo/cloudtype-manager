package org.github.guardjo.cloudtype.manager.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
@RequiredArgsConstructor
@Getter
public class JwtProperties {
    private final String secret;
    private final long accessTokenExpirationMillis;
    private final long refreshTokenExpirationMillis;
}
