package org.github.guardjo.cloudtype.manager.config.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoPrincipal implements UserDetails, OAuth2User {
    @Getter
    private final UserInfo userInfo;
    private final Map<String, Object> attributes;

    public static UserInfoPrincipal from(UserInfoEntity entity, Map<String, Object> attributes) {
        return new UserInfoPrincipal(UserInfo.from(entity), attributes);
    }

    public static UserInfoPrincipal from(UserInfoEntity entity) {
        return new UserInfoPrincipal(UserInfo.from(entity), Map.of());
    }

    @Override
    public String getName() {
        return userInfo.name();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userInfo.email();
    }

    @Override
    public String getUsername() {
        return userInfo.id();
    }
}
