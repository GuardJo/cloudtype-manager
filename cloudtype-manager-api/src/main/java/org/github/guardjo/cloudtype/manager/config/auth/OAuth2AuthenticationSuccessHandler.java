package org.github.guardjo.cloudtype.manager.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.properties.FrontendProperties;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final FrontendProperties frontendProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthTokenInfo authTokenInfo = jwtTokenProvider.generateAuthTokenInfo(authentication);

        String targetUrl = UriComponentsBuilder.fromUriString(frontendProperties.authCallbackUrl())
                .queryParam("accessToken", authTokenInfo.accessToken())
                .queryParam("refreshToken", authTokenInfo.refreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
