package com.beyond3.yyGang.cart.dto;

import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.nsupplement.NSupplement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartOptionDto {

    private Long cartOptionId;

    private Long nSupplementId;

    private String nSupplementName;

    private String brand;

    private int quantity;

    private int price;

    public static CartOptionDto fromCartOption(CartOption cartOption) {
        return CartOptionDto.builder()
                .cartOptionId(cartOption.getCartOptionID())
                .nSupplementId(cartOption.getNSupplement().getProductId())
                .nSupplementName(cartOption.getNSupplement().getProductName())
                .brand(cartOption.getNSupplement().getBrand())
                .quantity(cartOption.getQuantity())
                .price(cartOption.getPrice())
                .build();
    }


}
