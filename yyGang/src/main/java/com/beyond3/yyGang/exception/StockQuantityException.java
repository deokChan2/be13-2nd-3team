package com.beyond3.yyGang.exception;

import com.beyond3.yyGang.exception.message.ExceptionMessage;
import lombok.Getter;

@Getter
public class StockQuantityException extends BaseException {


    public StockQuantityException(ExceptionMessage message) {
        super(message);
    }


}
