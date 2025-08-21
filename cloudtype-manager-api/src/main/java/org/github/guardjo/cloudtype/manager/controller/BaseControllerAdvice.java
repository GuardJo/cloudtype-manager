package org.github.guardjo.cloudtype.manager.controller;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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
                .data("요청 데이터가 올바르지 않습니다.")
                .build();
    }

    @ExceptionHandler({
            JwtException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> handleUnauthorized(Exception e) {
        log.warn("Unauthorized Exception: {}", e.getMessage(), e);

        return BaseResponse.<String>builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED.name())
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<String> handleNotFound(Exception e) {
        log.warn("Not Found Exception: {}", e.getMessage(), e);

        return BaseResponse.<String>builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("Method Argument Not Valid Exception: {}", ex.getMessage(), ex);

        BaseResponse<String> errorResponse = BaseResponse.<String>builder()
                .status(((HttpStatus) status).name())
                .statusCode(status.value())
                .data(ex.getFieldError().getDefaultMessage())
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }
}
