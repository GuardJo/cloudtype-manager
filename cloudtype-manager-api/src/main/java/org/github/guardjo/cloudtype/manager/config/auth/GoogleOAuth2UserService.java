package org.github.guardjo.cloudtype.manager.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserInfoEntityRepository userInfoEntityRepository;

    @Override
    @Transactional
    public UserInfoPrincipal loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService = new DefaultOAuth2UserService();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = defaultUserService.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = String.valueOf(attributes.get("email"));
        String username = registrationId + ":" + email;

        UserInfoEntity userInfoEntity = userInfoEntityRepository.findById(username)
                .orElseGet(() -> {
                    log.info("New User Login, registrationId = {}, email = {}", registrationId, email);
                    return userInfoEntityRepository.save(UserInfoEntity.builder()
                            .username(username)
                            .name(String.valueOf(attributes.get("name")))
                            .password(email)
                            .build());
                });

        return UserInfoPrincipal.from(userInfoEntity, attributes);
    }
}
