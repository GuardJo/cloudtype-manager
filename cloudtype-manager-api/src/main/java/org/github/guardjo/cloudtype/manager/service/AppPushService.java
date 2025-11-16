package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;

public interface AppPushService {

    /**
     * 주어진 회원 객체와 appPush 토큰 정보를 기반으로 신규 AppPushToken Entity를 저장한다.
     *
     * @param tokenRequest token, device 정보
     * @param userInfo     요청 회원 정보
     */
    void saveAppPushToken(AppPushTokenRequest tokenRequest, UserInfo userInfo);
}
