package com.beyond3.yyGang.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class CartListDto {
    // null조건 추가
    private int quantity;

    private int price;

    private String productName;

    private String brand;


}
