package org.github.guardjo.cloudtype.manager.service;

public interface RefreshTokenService {
    /**
     * 주어진 refresh Token과 user_info 식별자를 기준으로 refresh_token 인스턴스를 저장한다.
     *
     * @param token    refresh_token
     * @param username user_info 식별키
     */
    void saveNewToken(String token, String username);
}
