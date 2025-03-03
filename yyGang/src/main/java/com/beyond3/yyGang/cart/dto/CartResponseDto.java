package com.beyond3.yyGang.cart.dto;


import com.beyond3.yyGang.cart.Cart;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponseDto {

    private Long cartId;

    private List<CartOptionDto> cartOptions = new ArrayList<>();

    private int totalPrice;

    private CartResponseDto(Cart cart, List<CartOptionDto> cartOptions) {
        this.cartId = cart.getCartId();
        this.cartOptions = cartOptions;
        this.totalPrice = calculateCartTotalPrice(this.cartOptions);
    }

    public static CartResponseDto fromCart(Cart cart, List<CartOptionDto> cartOptions) {
        return new CartResponseDto(cart, cartOptions);
    }

    private int calculateCartTotalPrice(List<CartOptionDto> cartOptions) {
        return cartOptions.stream().mapToInt(CartOptionDto::getPrice).sum();
    }
}
