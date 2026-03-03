package org.github.guardjo.cloudtype.manager.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"statusCode", "status", "data"})
public class BaseResponse<T> {
    private int statusCode;
    private String status;
    private T data;

    public static <E> BaseResponse<E> of(HttpStatus status, E body) {
        return new BaseResponse<>(status.value(), status.name(), body);
    }

    public static BaseResponse<String> defaultSuccess() {
        return new BaseResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Successes");
    }

    public static void writeUnauthorizedResponse(HttpServletResponse response, AuthenticationException authenticationException, ObjectMapper objectMapper) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        BaseResponse<String> unauthorizedResponse = BaseResponse.of(HttpStatus.UNAUTHORIZED, authenticationException.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(unauthorizedResponse));
    }
}
