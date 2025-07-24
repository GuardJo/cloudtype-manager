package org.github.guardjo.cloudtype.manager.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("frontend")
public record FrontendProperties(
        String authCallbackUrl
) {
}
