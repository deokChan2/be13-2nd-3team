package com.beyond3.yyGang.exception;

import com.beyond3.yyGang.exception.message.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String type;
    private final HttpStatus status;

    protected BaseException(ExceptionMessage message) {
        super(message.getMessage());
        this.type = message.name();
        this.status = message.getStatus();
    }

}
