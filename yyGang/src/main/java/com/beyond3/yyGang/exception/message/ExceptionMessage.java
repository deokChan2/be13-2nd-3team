package com.beyond3.yyGang.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    STOCK_QUANTITY_NOT_ENOUGH("재고가 충분하지 않습니다.", HttpStatus.CONFLICT),
    CART_NOT_FOUND("유저의 장바구니를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CART_OPTION_NOT_FOUND("장바구니에서 영양제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_ENUM_VALUE("잘못된 값입니다.", HttpStatus.BAD_REQUEST); // 이거 맞는지 확인

    private final String message;

    private final HttpStatus status;
}
