package org.github.guardjo.cloudtype.manager.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("firebase")
public record FirebaseProperties(
        String credentialsPath
) {
}
