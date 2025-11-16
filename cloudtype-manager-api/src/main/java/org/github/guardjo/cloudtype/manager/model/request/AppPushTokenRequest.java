package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AppPushTokenRequest(
        @Schema(description = "접속 디바이스 정보", example = "android")
        String device,

        @Schema(description = "앱푸시 토큰 정보", example = "firebase-app-push-token")
        @NotBlank(message = "푸시 토큰 정보가 존재하지 않습니다.")
        String token
) {
}
