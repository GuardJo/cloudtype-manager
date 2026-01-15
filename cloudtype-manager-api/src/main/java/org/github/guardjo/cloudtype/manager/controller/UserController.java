package org.github.guardjo.cloudtype.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.service.AppPushService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController implements UserApiDoc {
    private final AppPushService appPushService;

    @GetMapping("/me")
    @Override
    public BaseResponse<UserInfo> getMyInfo(@AuthenticationPrincipal UserInfoPrincipal principal) {
        log.info("GET : /api/v1/users/me, username = {}", principal.getUsername());

        return BaseResponse.of(HttpStatus.OK, principal.getUserInfo());
    }

    @GetMapping("/me/fcm-token")
    @Override
    public BaseResponse<String> getMyFCMToken(@AuthenticationPrincipal UserInfoPrincipal principal, @Valid @RequestParam("deviceId") String deviceId) {
        log.info("GET : /api/v1/users/me/fcm-token, username = {}, deviceId = {}", principal.getUsername(), deviceId);

        return BaseResponse.of(HttpStatus.OK, appPushService.getAppPushToken(principal.getUsername(), deviceId));
    }
}
