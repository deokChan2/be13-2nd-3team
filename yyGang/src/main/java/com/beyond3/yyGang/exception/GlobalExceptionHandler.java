package com.beyond3.yyGang.exception;

import com.beyond3.yyGang.exception.dto.ApiErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponseDto> handleStockQuantityException(BaseException e) {

        return new ResponseEntity<>(new ApiErrorResponseDto(e.getStatus().value(), e.getType(), e.getMessage()), e.getStatus());
    }


}
