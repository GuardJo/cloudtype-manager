package org.github.guardjo.cloudtype.manager.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.github.guardjo.cloudtype.manager.config.TestSecurityConfig;
import org.github.guardjo.cloudtype.manager.config.auth.JwtTokenProvider;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.service.AppPushService;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    private final static UserInfoEntity TEST_USER_INFO = TestDataGenerator.userInfoEntity("tester");
    private final static UserInfoPrincipal TEST_USER_PRINCIPAL = UserInfoPrincipal.from(TEST_USER_INFO);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @MockitoBean
    private AppPushService appPushService;

    @DisplayName("GET : /api/v1/users/me")
    @Test
    void test_getMyInfo() throws Exception {
        UserInfo expected = TEST_USER_PRINCIPAL.getUserInfo();

        String response = mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(TEST_USER_PRINCIPAL)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, UserInfo.class);

        BaseResponse<UserInfo> actual = objectMapper.readValue(response, responseType);

        assertThat(actual).isNotNull();
        assertThat(actual.getData()).isNotNull();
        assertThat(actual.getData()).isEqualTo(expected);
    }

    @DisplayName("GET : /api/v1/users/me/fcm-token")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void test_getMyFCMToken(boolean hasData) throws Exception {
        String deviceId = "test-device-id";
        String fcmToken = "test-fcm-token";

        BaseResponse<String> expected = BaseResponse.of(HttpStatus.OK, fcmToken);

        if (hasData) {
            given(appPushService.getAppPushToken(eq(TEST_USER_INFO.getUsername()), eq(deviceId))).willReturn(fcmToken);
        } else {
            given(appPushService.getAppPushToken(eq(TEST_USER_INFO.getUsername()), eq(deviceId))).willThrow(EntityNotFoundException.class);
        }

        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/me/fcm-token")
                        .param("deviceId", deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(TEST_USER_PRINCIPAL)))
                .andDo(print())
                .andExpect(hasData ? status().isOk() : status().isNotFound());

        if (hasData) {
            String response = resultActions
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            JavaType responseType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, String.class);
            BaseResponse<String> actual = objectMapper.readValue(response, responseType);

            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }

        then(appPushService).should().getAppPushToken(eq(TEST_USER_INFO.getUsername()), eq(deviceId));
    }
}