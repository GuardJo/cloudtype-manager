package org.github.guardjo.cloudtype.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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

    @Async("healthCheckExecutor")
    @Override
    public CompletableFuture<Boolean> isServerActive(String healthCheckUrl) {
        log.debug("checking server status, url = {}", healthCheckUrl);

        return healthCheckClient.get()
                .uri(healthCheckUrl)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) {
                        log.debug("serverStatus is OK");
                        return CompletableFuture.completedFuture(true);
                    } else {
                        log.warn("serverStatus is {}", response.getStatusCode());
                        return CompletableFuture.completedFuture(false);
                    }
                });
    }
}
