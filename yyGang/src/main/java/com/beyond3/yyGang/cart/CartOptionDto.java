package com.beyond3.yyGang.cart;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.NSupplementRepository;
import lombok.Data;

@Data
public class CartOptionDto {

    private Long nSupplementId;    // 영양제 아이디

    private int quantity; // 수량

    private int price;  // 가격

}