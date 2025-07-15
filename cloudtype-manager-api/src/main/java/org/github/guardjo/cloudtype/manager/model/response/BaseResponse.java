package org.github.guardjo.cloudtype.manager.model.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
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
}
