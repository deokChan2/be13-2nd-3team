package com.beyond3.yyGang.cart;

import lombok.Data;

@Data
public class CartOptionDto {

    private Long nSupplementId;    // 영양제 아이디

    private int quantity; // 수량

    private int price;  // 가격

}