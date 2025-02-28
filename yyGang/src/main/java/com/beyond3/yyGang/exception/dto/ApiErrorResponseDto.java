package com.beyond3.yyGang.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponseDto {

    private final int code;
    private final String status;
    private final String message;

}
