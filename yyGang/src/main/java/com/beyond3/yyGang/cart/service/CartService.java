package com.beyond3.yyGang.cart.service;

import com.beyond3.yyGang.cart.Cart;
import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.cart.repository.CartOptionRepository;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.NSupplementRepository;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public Long saveCartOption(Long userId, Long nSupplementId, int quantity) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found for userId"));
        NSupplement findNSupplement = nSupplementRepository.findById(nSupplementId).orElseThrow(() -> new EntityNotFoundException("NSupplement not found for nSupplementId"));
        Cart findCart = cartRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Cart not found for userId"));

        CartOption cartOption = CartOption.createCartOption(findCart, findNSupplement, quantity);

        CartOption saveCartOption = cartOptionRepository.save(cartOption);

        return saveCartOption.getCartOptionID();
    }

    // void와 Long중 어느 거 사용, user를 파라미터로 받아와야 하는지???
    // List
    @Transactional
    public void deleteCartOption(Long cartOptionId/*, Long userId*/) {
        // User findUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found for userId"));
        CartOption findCartOption = cartOptionRepository.findById(cartOptionId).orElseThrow(() -> new EntityNotFoundException("CartOption not found for CartOptionId"));
        //Cart findCart = cartRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Cart not found for userId"));

        cartOptionRepository.delete(findCartOption);
    }

    @Transactional
    public void increaseCartOptionQuantity(Long cartOptionId, int quantity) {
        CartOption findCartOption = cartOptionRepository.findById(cartOptionId).orElseThrow(() -> new EntityNotFoundException("CartOption not found for CartOptionId"));
        findCartOption.increaseQuantity(quantity);
    }

    @Transactional
    public void decreaseCartOptionQuantity(Long cartOptionId, int quantity) {
        CartOption findCartOption = cartOptionRepository.findById(cartOptionId).orElseThrow(() -> new EntityNotFoundException("CartOption not found for CartOptionId"));
        findCartOption.decreaseQuantity(quantity);
    }


}
