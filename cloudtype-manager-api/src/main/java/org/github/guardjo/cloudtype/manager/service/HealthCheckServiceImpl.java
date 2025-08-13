package org.github.guardjo.cloudtype.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class HealthCheckServiceImpl implements HealthCheckService {
    private final RestClient healthCheckClient;

    public HealthCheckServiceImpl(RestClient.Builder restClientBuilder) {
        this.healthCheckClient = restClientBuilder.build();
    }

    @Override
    public CompletableFuture<Boolean> isServerActive(String healthCheckUrl) {
        log.debug("checking server status, url = {}", healthCheckUrl);

        return healthCheckClient.get()
                .uri(healthCheckUrl)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return CompletableFuture.completedFuture(true);
                    } else {
                        return CompletableFuture.completedFuture(false);
                    }
                });
    }
}
