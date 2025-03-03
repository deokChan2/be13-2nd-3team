package com.beyond3.yyGang.exception;

import com.beyond3.yyGang.exception.message.ExceptionMessage;

public class InvalidEnumValueException extends BaseException {

    public InvalidEnumValueException(ExceptionMessage message) {
        super(message);
    }
}
