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
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return CartResponseDto.fromCart(findCart.getCartId(), List.of(cartOptionDto));
    }

    @Transactional
    public void deleteCartOption(Long cartOptionId/*, Long userId*/) {
        CartOption findCartOption = cartOptionRepository.findById(cartOptionId).orElseThrow(() -> new EntityNotFoundException("CartOption not found for CartOptionId"));

        cartOptionRepository.delete(findCartOption);
    }

    @Transactional
    // 장바구니 상품 수량, 가격 업데이트
    public CartOptionDto updateCartProduct(Long cartOptionId, int count) {
        CartOption cartOption = cartOptionRepository.findById(cartOptionId)
                .orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_OPTION_NOT_FOUND));
        NSupplement nSupplement = cartOption.getNSupplement();

        // 재고 확인
        if (nSupplement.getStockQuantity() < count) {
            throw new StockQuantityException(ExceptionMessage.STOCK_QUANTITY_NOT_ENOUGH);
        }

        // 수량 확인
        /*if (count < 1) {
            throw new CartCountException(ExceptionMessage.CART_COUNT_MIN_VIOLATION);
        }

        cartOption.updateSupplement(count);*/
        return CartOptionDto.fromCartOption(cartOption);
    }

    @Transactional
    public CartResponseDto getCart(String userEmail, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 사용자 검증 우선
        User findUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found for userEmail"));

        // 해당 사용자의 Cart가 있는지 확인
        Cart byUserEmail = cartRepository.findByUserEmail(findUser.getEmail())
                .orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        // 카트에 있는 카트옵션들을 가져올거라 패치조인 적용
//        Cart findCart = cartRepository.findByUserEmailWithCartOptions(userEmail)
//                .orElseThrow(() -> new CartEntityException(ExceptionMessage.CART_NOT_FOUND));

        // 페이징 처리
        Page<CartOption> findCart = cartRepository.findCartOptionByUserEmail(userEmail, pageable);

        // 찾아온 Cart에서 CartOptions 추출 -> Dto로 변경해서 반환
        List<CartOptionDto> cartOptionDtoList = findCart.getContent().stream()
                .map(CartOptionDto::fromCartOption)
                .toList();

        return CartResponseDto.fromCart(byUserEmail.getCartId(), cartOptionDtoList);
    }
}
