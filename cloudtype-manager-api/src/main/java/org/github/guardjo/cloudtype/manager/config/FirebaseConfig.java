package org.github.guardjo.cloudtype.manager.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.properties.FirebaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseConfig {
    private final FirebaseProperties firebaseProperties;
    private final ResourceLoader resourceLoader;

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        GoogleCredentials credentials = null;
        try {
            Resource resource = resourceLoader.getResource(firebaseProperties.credentialsPath());

            try (InputStream credentialStream = resource.getInputStream()) {
                credentials = GoogleCredentials.fromStream(credentialStream);
            }
        } catch (IOException e) {
            log.error("Failed load FirebaseCredentials, cause = {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance();
    }
}
