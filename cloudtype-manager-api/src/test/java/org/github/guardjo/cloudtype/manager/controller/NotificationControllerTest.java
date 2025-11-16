package org.github.guardjo.cloudtype.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.guardjo.cloudtype.manager.config.auth.JwtTokenProvider;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.service.AppPushService;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
class NotificationControllerTest {
    private final static UserInfoPrincipal TEST_USER = UserInfoPrincipal.from(TestDataGenerator.userInfoEntity("Tester"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppPushService appPushService;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @DisplayName("POST : /api/v1/notifications/push-token")
    @Test
    void test_addPushToken() throws Exception {
        String device = "WEB";
        String token = "app-push-token";
        AppPushTokenRequest tokenRequest = new AppPushTokenRequest(device, token);
        String requestContent = objectMapper.writeValueAsString(tokenRequest);

        willDoNothing().given(appPushService).saveAppPushToken(eq(tokenRequest), eq(TEST_USER.getUserInfo()));

        mockMvc.perform(post("/api/v1/notifications/push-token")
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user(TEST_USER)))
                .andDo(print())
                .andExpect(status().isOk());

        then(appPushService).should().saveAppPushToken(eq(tokenRequest), eq(TEST_USER.getUserInfo()));
    }
}