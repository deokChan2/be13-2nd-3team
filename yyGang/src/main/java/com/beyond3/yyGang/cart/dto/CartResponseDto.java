package com.beyond3.yyGang.cart.dto;


import com.beyond3.yyGang.cart.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {

    private Long cartId;

    private List<CartOptionDto> cartOptions;

    // private int totalPrice;

    public static CartResponseDto fromCart(Cart cart, List<CartOptionDto> cartOptions) {
        return CartResponseDto.builder()
                .cartId(cart.getCartId())
                .cartOptions(cartOptions)
                .build();
    }
}
