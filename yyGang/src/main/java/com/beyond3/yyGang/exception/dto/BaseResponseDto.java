package com.beyond3.yyGang.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class BaseResponseDto<T> {

    @Schema(description = "응답 코드", example = "200")
    protected final int code;

    @Schema(description = "응답 메세지", example = "OK")
    protected final String message;

    @Schema(description = "응답 데이터")
    protected final List<T> items;

    public BaseResponseDto(HttpStatus status, T department) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.items = Collections.singletonList(department);
    }

    protected BaseResponseDto(HttpStatus status, List<T> items) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.items = items;
    }
}
