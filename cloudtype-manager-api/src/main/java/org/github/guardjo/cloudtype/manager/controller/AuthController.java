package org.github.guardjo.cloudtype.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.auth.JwtTokenProvider;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.RefreshTokenRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController implements AuthApiDoc {
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    @Override
    public BaseResponse<AuthTokenInfo> refreshAccessToken(@AuthenticationPrincipal UserInfoPrincipal principal, @RequestBody @Valid RefreshTokenRequest refreshRequest) {
        log.info("POST : /api/v1/auth/refresh, username = {}", principal.getUsername());

        AuthTokenInfo authTokenInfo = jwtTokenProvider.generateAuthTokenInfo(refreshRequest.refreshToken(), principal.getUsername());

        return BaseResponse.of(HttpStatus.OK, authTokenInfo);
    }
}
