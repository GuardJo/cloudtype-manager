package org.github.guardjo.cloudtype.manager.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;

import java.time.LocalDateTime;

/* 서버 상태 변경 이력 요약 VO */
public record ServerStatusChangeHistorySummary(
        @Schema(description = "서버 상태 변경 이력 식별키", example = "1")
        Long statusChangeHistoryId,

        @Schema(description = "서버명", example = "Test Server")
        String serverName,

        @Schema(description = "서버 상태 확인 URL", example = "https://naver.com")
        String healthCheckUrl,

        @Schema(description = "상태 변경 시각", example = "2026-02-21 19:00:00")
        LocalDateTime checkedAt,

        @Schema(description = "최근 변겅 상태", example = "true")
        boolean status
) {
    public static ServerStatusChangeHistorySummary of(ServerStatusChangeHistoryEntity entity) {
        ServerInfoEntity serverInfo = entity.getServer();
        return new ServerStatusChangeHistorySummary(
                entity.getId(),
                serverInfo.getServerName(),
                serverInfo.getHealthCheckUrl(),
                entity.getCheckedAt(),
                entity.isUp()
        );
    }
}
