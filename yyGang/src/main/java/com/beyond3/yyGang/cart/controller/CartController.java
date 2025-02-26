package com.beyond3.yyGang.cart.controller;

import com.beyond3.yyGang.cart.Cart;
import com.beyond3.yyGang.cart.dto.CartListDto;
import com.beyond3.yyGang.cart.repository.CartOptionRepository;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.cart.service.CartService;
import com.beyond3.yyGang.security.JwtTokenProvider;
import com.beyond3.yyGang.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartOptionRepository cartOptionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 이게 맞나??
    // 로그인한 사용자의 장바구니 조회
    @GetMapping
    public ResponseEntity<List<CartListDto>> getUserCart(@RequestHeader("Authorization") String token) {
        String userEmail = getUserEmailFromToken(token);

        Cart findCart = cartRepository.findByUserEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("cart not found"));
        List<CartListDto> cartListDtoByCartId = cartOptionRepository.findCartListDtoByCartId(findCart.getCartId());

        return ResponseEntity.ok(cartListDtoByCartId);
    }

    @PostMapping("/nsupplement")
    public Res








    private String getUserEmailFromToken(String token){
        String trimToken = token.substring(7).trim();

        if(!jwtTokenProvider.validateToken(trimToken)){
            // 토큰이 유효하지 않은 경우
            throw new UsernameNotFoundException("유효하지 않은 토큰입니다.");
        }
        return jwtTokenProvider.getAuthentication(trimToken).getName();
    }

}
