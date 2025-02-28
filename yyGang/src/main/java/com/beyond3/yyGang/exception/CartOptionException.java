package com.beyond3.yyGang.exception;

import com.beyond3.yyGang.exception.message.ExceptionMessage;
import lombok.Getter;

@Getter
public class CartOptionException extends BaseException {
    public CartOptionException(ExceptionMessage message) {
        super(message);
    }
}
