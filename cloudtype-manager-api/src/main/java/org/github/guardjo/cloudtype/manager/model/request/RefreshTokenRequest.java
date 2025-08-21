package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @Schema(description = "refresh-token", example = "refresh-token(JWT)")
        @NotBlank(message = "token 값이 비어 있습니다.")
        String refreshToken
) {
}
