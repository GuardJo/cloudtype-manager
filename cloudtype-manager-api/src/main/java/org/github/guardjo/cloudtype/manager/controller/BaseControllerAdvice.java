package org.github.guardjo.cloudtype.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "org.github.guardjo.cloudtype.manager.controller")
@Slf4j
public class BaseControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleBadRequest(Exception e) {
        log.warn("Bad Request Exception: {}", e.getMessage(), e);

        return BaseResponse.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public BaseResponse<String> handleConflict(Exception e) {
        log.warn("Conflict Exception: {}", e.getMessage(), e);

        return BaseResponse.<String>builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.name())
                .data("이미 사용중인 이름입니다.")
                .build();
    }
}
