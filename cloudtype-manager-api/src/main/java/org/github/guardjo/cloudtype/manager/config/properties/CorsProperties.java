package org.github.guardjo.cloudtype.manager.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(
        List<String> allowOrigins,
        List<String> allowMethods,
        List<String> allowHeaders,
        boolean allowCredentials
) {
}
