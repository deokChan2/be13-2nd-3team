package com.beyond3.yyGang.exception;

import com.beyond3.yyGang.exception.message.ExceptionMessage;
import lombok.Getter;

@Getter
public class CartEntityException extends BaseException {

    public CartEntityException(ExceptionMessage message) {
        super(message);
    }

}
