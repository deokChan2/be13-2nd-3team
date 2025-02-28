package com.beyond3.yyGang.cart.service;

import com.beyond3.yyGang.cart.Cart;
import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.cart.dto.AddCartOptionRequestDto;
import com.beyond3.yyGang.cart.dto.CartOptionDto;
import com.beyond3.yyGang.cart.dto.CartResponseDto;
import com.beyond3.yyGang.cart.repository.CartOptionRepository;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.exception.CartEntityException;
import com.beyond3.yyGang.exception.CartOptionException;
import com.beyond3.yyGang.exception.StockQuantityException;
import com.beyond3.yyGang.exception.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.NSupplementRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartOptionRepository cartOptionRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final NSupplementRepository nSupplementRepository;

    @Transactional
    public CartResponseDto addCartOption(String userEmail, AddCartOptionRequestDto addCartOptionRequestDto) {
        User findUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found for userId"));
        NSupplement findNSupplement = nSupplementRepository.findById(addCartOptionRequestDto.getNSupplementId()).orElseThrow(() -> new EntityNotFoundException("NSupplement not found for nSupplementId"));
        Cart findCart = cartRepository.findByUserEmail(userEmail).orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        // 재고 확인
        if (findNSupplement.getStockQuantity() < addCartOptionRequestDto.getQuantity()) {
            throw new StockQuantityException(ExceptionMessage.STOCK_QUANTITY_NOT_ENOUGH);
        }

        // 카트 옵션 생성
        CartOption cartOption = CartOption.createCartOption(findCart, findNSupplement, addCartOptionRequestDto.getQuantity());

        // 카트 옵션 저장
        CartOption saveCartOption = cartOptionRepository.save(cartOption);

        // 카트 옵션 DTO 생성
        CartOptionDto cartOptionDto = CartOptionDto.fromCartOption(saveCartOption);

        // 카트 DTO 리턴
        return CartResponseDto.fromCart(findCart, List.of(cartOptionDto));
    }

    @Transactional
    public void deleteCartOption(Long cartOptionId/*, Long userId*/) {
        // User findUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found for userId"));
        CartOption findCartOption = cartOptionRepository.findById(cartOptionId).orElseThrow(() -> new CartOptionException(ExceptionMessage.CART_OPTION_NOT_FOUND));
        //Cart findCart = cartRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Cart not found for userId"));

        cartOptionRepository.delete(findCartOption);
    }

    @Transactional
    // 장바구니 상품 수량, 가격 업데이트
    public void updateCartProduct(Long cartOptionId, int count) {
        CartOption cartOption = cartOptionRepository.findById(cartOptionId).orElseThrow(() -> new CartOptionException(ExceptionMessage.CART_OPTION_NOT_FOUND));
        cartOption.updateQuantity(count);
    }

    public CartResponseDto getCart(String userEmail) {
        User findUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found for userEmail"));

        // 카트에 있는 카트옵션들을 가져올거라 패치조인 적용
        Cart findCart = cartRepository.findByUserEmailWithCartOptions(userEmail).orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        List<CartOption> cartOptions = findCart.getCartOptions();

        List<CartOptionDto> cartOptionDtoList = cartOptions.stream().map(cartOption ->
                CartOptionDto.fromCartOption(cartOption)).toList();

        return CartResponseDto.fromCart(findCart, cartOptionDtoList);
    }
}
