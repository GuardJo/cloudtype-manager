package org.github.guardjo.cloudtype.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.auth.JwtTokenProvider;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.LogoutRequest;
import org.github.guardjo.cloudtype.manager.model.request.RefreshTokenRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    public BaseResponse<AuthTokenInfo> refreshAccessToken(@RequestBody @Valid RefreshTokenRequest refreshRequest) {
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshRequest.refreshToken());
        UserInfoPrincipal principal = (UserInfoPrincipal) authentication.getPrincipal();

        log.info("POST : /api/v1/auth/refresh, username = {}", principal.getUsername());

        AuthTokenInfo authTokenInfo = jwtTokenProvider.generateAuthTokenInfo(refreshRequest.refreshToken(), principal.getUsername());

        return BaseResponse.of(HttpStatus.OK, authTokenInfo);
    }

    @PostMapping("/logout")
    @Override
    public BaseResponse<String> logout(@AuthenticationPrincipal UserInfoPrincipal principal, @RequestBody LogoutRequest logoutRequest) {
        log.info("POST : /api/v1/auth/logout, request = {}", logoutRequest);

        if (!principal.getUsername().equals(logoutRequest.username())) {
            log.warn("Logout request username is not same as principal username");
            throw new AccessDeniedException("Logout request username is not same as principal username");
        }

        jwtTokenProvider.invalidateAuthToken(new AuthTokenInfo(logoutRequest.accessToken(), logoutRequest.refreshToken()));

        return BaseResponse.defaultSuccess();
    }
}
