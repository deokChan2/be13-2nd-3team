package com.beyond3.yyGang.cart.controller;

import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.cart.dto.AddCartOptionRequestDto;
import com.beyond3.yyGang.cart.dto.CartOptionDto;
import com.beyond3.yyGang.cart.dto.CartResponseDto;
import com.beyond3.yyGang.cart.repository.CartOptionRepository;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.cart.service.CartService;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartOptionRepository cartOptionRepository;
    private final NSupplementRepository nSupplementRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인한 사용자의 장바구니 조회
    @GetMapping("/")
    @Operation(summary = "장바구니 목록 조회", description = "사용자 장바구니의 영양제 목록을 조회한다.")
    public ResponseEntity<CartResponseDto> getUserCart(@RequestHeader("Authorization") String token, @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        String userEmail = getUserEmailFromToken(token);

        CartResponseDto cartResponseDto = cartService.getCart(userEmail, page, size);

        return ResponseEntity.ok(cartResponseDto);
    }

    @PostMapping("/nsupplement")
    @Operation(summary = "장바구니 영양제 추가", description = "사용자 장바구니에 영양제를 추가한다.")
    public ResponseEntity<CartResponseDto> addCartOption(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AddCartOptionRequestDto addCartOptionRequestDto
    ) {
        String userEmail = getUserEmailFromToken(token);

        CartResponseDto cartResponseDto = cartService.addCartOption(userEmail, addCartOptionRequestDto);

        return ResponseEntity.ok(cartResponseDto);
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartOptionId}")
    @Operation(summary = "장바구니 영양제 삭제", description = "사용자 장바구니에 있는 영양제를 삭제한다.")
    public ResponseEntity<Long> deleteCartOption(@PathVariable Long cartOptionId
            /*,@RequestHeader("Authorization") String token*/) {

        cartService.deleteCartOption(cartOptionId);
        return ResponseEntity.ok(cartOptionId);
    }

    // 장바구니 상품 수량,가격 변경
    @PutMapping("/{cartOptionId}")
    @Operation(summary = "장바구니 영양제 수량 변경", description = "사용자 장바구니의 영양제 수량을 변경한다.")
    public ResponseEntity<CartOptionDto> updateCartOptionQuantity(@PathVariable Long cartOptionId,
                                                                  @RequestParam int quantity
            /*,@RequestHeader("Authorization") String token*/) {

        // 수량, 가격 업데이트
        CartOptionDto cartOptionDto = cartService.updateCartProduct(cartOptionId, quantity);
        return ResponseEntity.ok(cartOptionDto);
    }

    private String getUserEmailFromToken(String token){
        String trimToken = token.substring(7).trim();

        if(!jwtTokenProvider.validateToken(trimToken)){
            // 토큰이 유효하지 않은 경우
            throw new UsernameNotFoundException("유효하지 않은 토큰입니다.");
        }
        return jwtTokenProvider.getAuthentication(trimToken).getName();
    }

}
