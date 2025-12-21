package org.github.guardjo.cloudtype.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController implements UserApiDoc {

    @GetMapping("/me")
    @Override
    public BaseResponse<UserInfo> getMyInfo(@AuthenticationPrincipal UserInfoPrincipal principal) {
        log.info("GET : /api/v1/users/me, username = {}", principal.getUsername());

        return BaseResponse.of(HttpStatus.OK, principal.getUserInfo());
    }
}
