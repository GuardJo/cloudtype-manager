package org.github.guardjo.cloudtype.manager.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.github.guardjo.cloudtype.manager.config.auth.JwtTokenProvider;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.service.ServerManagementService;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ServerManagementController.class)
class ServerManagementControllerTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final static UserInfoPrincipal TEST_USER_DETAILS = UserInfoPrincipal.from(TEST_USER);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServerManagementService serverManagementService;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @DisplayName("GET : /api/v1/servers")
    @Test
    void test_getServers() throws Exception {
        UserInfo userInfo = TEST_USER_DETAILS.getUserInfo();
        List<ServerSummary> expected = Stream.of(
                        TestDataGenerator.serverInfoEntity(1L, "Server 1", TEST_USER),
                        TestDataGenerator.serverInfoEntity(2L, "Server 2", TEST_USER)
                )
                .map(ServerSummary::of)
                .toList();

        given(serverManagementService.getServerSummaries(eq(userInfo))).willReturn(expected);

        String response = mockMvc.perform(get("/api/v1/servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(TEST_USER_DETAILS)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, ServerSummary.class);
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, listType);

        BaseResponse<List<ServerSummary>> actual = objectMapper.readValue(response, responseType);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.name());
        assertThat(actual.getData()).isEqualTo(expected);

        then(serverManagementService).should().getServerSummaries(eq(userInfo));
    }

    @DisplayName("POST : /api/v1/servers")
    @ParameterizedTest
    @MethodSource("getAddNewServerTestData")
    void test_addNewServer(CreateServerRequest createServerRequest, int expectedStatusCode) throws Exception {
        willDoNothing().given(serverManagementService).addServer(eq(createServerRequest), eq(TEST_USER_DETAILS.getUserInfo()));

        mockMvc.perform(post("/api/v1/servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createServerRequest))
                        .with(user(TEST_USER_DETAILS))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(expectedStatusCode));

        if (expectedStatusCode == HttpStatus.OK.value()) {
            then(serverManagementService).should().addServer(eq(createServerRequest), eq(TEST_USER_DETAILS.getUserInfo()));
        } else {
            then(serverManagementService).should(never()).addServer(eq(createServerRequest), eq(TEST_USER_DETAILS.getUserInfo()));
        }
    }

    @DisplayName("POST : /api/v1/servers -> Conflict Error")
    @Test
    void test_addNewServer_return_duplicate_key_exception() throws Exception {
        CreateServerRequest createServerRequest = new CreateServerRequest(
                "Test Server",
                "https://naver.com",
                "https://google.com"
        );
        willThrow(DataIntegrityViolationException.class).given(serverManagementService).addServer(eq(createServerRequest), eq(TEST_USER_DETAILS.getUserInfo()));

        mockMvc.perform(post("/api/v1/servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createServerRequest))
                        .with(csrf())
                        .with(user(TEST_USER_DETAILS)))
                .andDo(print())
                .andExpect(status().isConflict());

        then(serverManagementService).should().addServer(eq(createServerRequest), eq(TEST_USER_DETAILS.getUserInfo()));
    }


    @DisplayName("GET : /api/v1/servers/{serverId}")
    @Test
    void test_getServerDetail() throws Exception {
        Long serverId = 1L;
        ServerInfoEntity serverInfoEntity = TestDataGenerator.serverInfoEntity(serverId, "Test server", TEST_USER);
        ServerDetail expected = ServerDetail.from(serverInfoEntity);

        given(serverManagementService.getServerDetail(eq(serverId), eq(TEST_USER_DETAILS.getUserInfo()))).willReturn(expected);

        String response = mockMvc.perform(get("/api/v1/servers/" + serverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(TEST_USER_DETAILS)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, ServerDetail.class);
        BaseResponse<ServerDetail> actual = objectMapper.readValue(response, responseType);

        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.name());
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual.getData()).isEqualTo(expected);

        then(serverManagementService).should().getServerDetail(eq(serverId), eq(TEST_USER_DETAILS.getUserInfo()));
    }


    @DisplayName("GET : /api/v1/servers/{serverId} -> Not Found")
    @Test
    void test_getServerDetail_not_found_exception() throws Exception {
        Long serverId = 1L;

        given(serverManagementService.getServerDetail(eq(serverId), eq(TEST_USER_DETAILS.getUserInfo()))).willThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/servers/" + serverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(TEST_USER_DETAILS)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(serverManagementService).should().getServerDetail(eq(serverId), eq(TEST_USER_DETAILS.getUserInfo()));
    }

    private static Stream<Arguments> getAddNewServerTestData() {
        return Stream.of(
                Arguments.of(
                        new CreateServerRequest(
                                "Test Server",
                                "https://naver.com",
                                "https://google.com"
                        ),
                        HttpStatus.OK.value()
                ),
                Arguments.of(
                        new CreateServerRequest(
                                "Test Server",
                                "https://naver.com",
                                ""
                        ),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }
}