package org.github.guardjo.cloudtype.manager.model.vo;

public record AuthTokenInfo(
        String accessToken,
        String refreshToken
) {
}
