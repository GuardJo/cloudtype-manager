package org.github.guardjo.cloudtype.manager.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;

/* 서버 요약 정보 VO */
public record ServerSummary(
        @Schema(description = "서버 식별키", example = "1")
        Long serverId,

        @Schema(description = "서버명", example = "서버 1")
        String serverName,

        @Schema(description = "활성화 여부", example = "true")
        boolean activate
) {
    public static ServerSummary of(ServerInfoEntity entity) {
        return new ServerSummary(
                entity.getId(),
                entity.getServerName(),
                entity.isActivate()
        );
    }
}
