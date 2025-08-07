package org.github.guardjo.cloudtype.manager.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;

/* 서버 상세 정보 VO */
public record ServerDetail(
        @Schema(description = "서버 식별키", example = "1")
        Long serverId,

        @Schema(description = "서버명", example = "서버 1")
        String serverName,

        @Schema(description = "서버 활성 상태", example = "true")
        boolean activate,

        @Schema(description = "호스트 URL", example = "https://naver.com")
        String hostingUrl,

        @Schema(description = "관리 화면 URL", example = "https://cloudtype.io")
        String managementUrl
) {
    public static ServerDetail from(ServerInfoEntity entity) {
        return new ServerDetail(
                entity.getId(),
                entity.getServerName(),
                entity.isActivate(),
                entity.getHostingUrl(),
                entity.getManagementUrl()
        );
    }
}
