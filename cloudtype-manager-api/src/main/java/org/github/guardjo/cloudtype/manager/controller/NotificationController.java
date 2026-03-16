package org.github.guardjo.cloudtype.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.service.AppPushService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController implements NotificationApiDoc {
    private final AppPushService appPushService;

    @PostMapping("/push-token")
    @Override
    public BaseResponse<String> addPushToken(@AuthenticationPrincipal UserInfoPrincipal principal,
                                             @RequestBody @Valid AppPushTokenRequest appPushTokenRequest) {
        log.info("POST : /api/v1/notifications/push-token, username = {}, token = {}, device = {}",
                principal.getUsername(), appPushTokenRequest.token(), appPushTokenRequest.device());

        appPushService.saveAppPushToken(appPushTokenRequest, principal.getUserInfo());

        return BaseResponse.defaultSuccess();
    }


    @PatchMapping("/push-token")
    @Override
    public BaseResponse<String> updatePushToken(@AuthenticationPrincipal UserInfoPrincipal principal,
                                                @RequestBody @Valid AppPushTokenRequest request) {
        log.info("PATCH : /api/v1/notifications/push-token, username = {}, token = {}, device = {}", principal.getUsername(), request.token(), request.device());

        // TODO 기능 연동하기

        return BaseResponse.defaultSuccess();
    }
}
