package org.github.guardjo.cloudtype.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;
import org.github.guardjo.cloudtype.manager.repository.ServerStatusChangeHistoryEntityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class HealthCheckServiceImpl implements HealthCheckService {
    private final RestClient healthCheckClient;
    private final ServerStatusChangeHistoryEntityRepository serverStatusChangeHistoryRepository;

    public HealthCheckServiceImpl(RestClient.Builder restClientBuilder, ServerStatusChangeHistoryEntityRepository serverStatusChangeHistoryRepository) {
        this.healthCheckClient = restClientBuilder.build();
        this.serverStatusChangeHistoryRepository = serverStatusChangeHistoryRepository;
    }

    @Transactional
    @Async("healthCheckExecutor")
    @Override
    public CompletableFuture<Boolean> isServerActive(ServerInfoEntity serverInfoEntity) {
        String healthCheckUrl = serverInfoEntity.getHealthCheckUrl();
        log.debug("checking server status, url = {}", healthCheckUrl);

        return Objects.requireNonNull(healthCheckClient.get()
                        .uri(healthCheckUrl)
                        .exchange((request, response) -> {
                            boolean resultStatus = false;
                            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) {
                                log.debug("serverStatus is OK");
                                resultStatus = true;
                            } else {
                                log.warn("serverStatus is {}", response.getStatusCode());
                            }

                            if (serverInfoEntity.isActivate() != resultStatus) {
                                addStatusChangeHistory(response, serverInfoEntity, resultStatus);
                            }

                            return CompletableFuture.completedFuture(resultStatus);
                        }))
                .exceptionally(e -> {
                    log.warn("Failed health-check server, url = {}, cause = {}", healthCheckUrl, e.getMessage(), e);

                    addStatusChangeHistory(e, serverInfoEntity);

                    return false;
                });
    }

    /*
    서버 응답 데이터 기반 활성 상태 변경 이력 저장
     */
    private void addStatusChangeHistory(RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse clientResponse, ServerInfoEntity serverInfo, boolean currentStatus) throws IOException {
        log.debug("Save server status change, serverId = {}, lastStatus = {}, currentStatus = {}", serverInfo.getId(), serverInfo.isActivate(), currentStatus);

        ServerStatusChangeHistoryEntity serverStatusChangeHistoryEntity = ServerStatusChangeHistoryEntity.builder()
                .server(serverInfo)
                .isUp(currentStatus)
                .statusCode(clientResponse.getStatusCode().value())
                .responseBody(clientResponse.bodyTo(String.class))
                .responseHeaders(String.valueOf(clientResponse.getHeaders()))
                .build();

        serverStatusChangeHistoryRepository.save(serverStatusChangeHistoryEntity);
    }

    /*
    예외 발생 기반 활성 상태 변경 이력 저장ㄴ
     */
    private <T extends Throwable> void addStatusChangeHistory(T e, ServerInfoEntity serverInfo) {
        ServerStatusChangeHistoryEntity serverStatusChangeHistoryEntity = ServerStatusChangeHistoryEntity.builder()
                .server(serverInfo)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCategory(e.getMessage())
                .exceptionClass(e.getClass().getName())
                .exceptionMessage(e.getMessage())
                .stacktraceText(Arrays.toString(e.getStackTrace()))
                .build();

        serverStatusChangeHistoryRepository.save(serverStatusChangeHistoryEntity);
    }
}
