package org.github.guardjo.cloudtype.manager.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import org.github.guardjo.cloudtype.manager.config.auth.JwtTokenProvider;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.RefreshTokenRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final static UserInfoPrincipal TEST_USER_DETAILS = UserInfoPrincipal.from(TEST_USER);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @DisplayName("POST : /api/v1/auth/refresh")
    @ParameterizedTest
    @MethodSource("getRefreshAccessTokenTestData")
    void test_refreshAccessToken(String refreshToken, HttpStatus httpStatus) throws Exception {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);
        String requestContent = objectMapper.writeValueAsString(refreshRequest);

        AuthTokenInfo expected = new AuthTokenInfo("test-access-token", "test-refresh-token");
        given(tokenProvider.generateAuthTokenInfo(eq(refreshToken), eq(TEST_USER.getUsername()))).willReturn(expected);

        String response = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
                        .with(user(TEST_USER_DETAILS))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(httpStatus.value()))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        if (httpStatus.is2xxSuccessful()) {
            JavaType tokenInfoType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, AuthTokenInfo.class);
            BaseResponse<AuthTokenInfo> actual = objectMapper.readValue(response, tokenInfoType);

            assertThat(actual).isNotNull();
            assertThat(actual.getData()).isEqualTo(expected);

            then(tokenProvider).should().generateAuthTokenInfo(eq(refreshToken), eq(TEST_USER.getUsername()));
        }
    }

    @DisplayName("POST : /api/v1/auth/refresh -> Unauthorized")
    @Test
    void test_refreshAccessToken_unauthorized() throws Exception {
        String refreshToken = "test-token";
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);
        String requestContent = objectMapper.writeValueAsString(refreshRequest);

        given(tokenProvider.generateAuthTokenInfo(eq(refreshToken), eq(TEST_USER.getUsername()))).willThrow(MalformedJwtException.class);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
                        .with(user(TEST_USER_DETAILS))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        then(tokenProvider).should().generateAuthTokenInfo(eq(refreshToken), eq(TEST_USER.getUsername()));
    }

    private static Stream<Arguments> getRefreshAccessTokenTestData() {
        return Stream.of(
                Arguments.of("test-token", HttpStatus.OK),
                Arguments.of("", HttpStatus.BAD_REQUEST)
        );
    }
}