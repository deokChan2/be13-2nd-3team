package com.beyond3.yyGang.order.service;

import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.NSupplementRepository;
import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.order.OrderOption;
import com.beyond3.yyGang.order.repository.OrderRepository;
import com.beyond3.yyGang.pay.PersonalAccount;
import com.beyond3.yyGang.pay.repository.PersonalAccountRepository;
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
public class OrderService {

    private final UserRepository userRepository;
    private final NSupplementRepository nSupplementRepository;
    private final OrderRepository orderRepository;
    private final PersonalAccountRepository personalAccountRepository;


    // 재산 확인
    @Transactional
    public Long orderOne(Long userId, Long nSupplementId, int quantity) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found for userId"));
        NSupplement findNSupplement = nSupplementRepository.findById(nSupplementId).orElseThrow(() -> new EntityNotFoundException("NSupplement not found for nSupplementId"));
        PersonalAccount findPersonalAccount = personalAccountRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("PersonalAccount not found for userId"));

        // 재산 충분한지 확인
        if (findPersonalAccount.getBalance() < findNSupplement.getPrice() * quantity) {
            throw new IllegalStateException("재산이 부족합니다.");
        }

        // 재고 부족 시 예외처리
        if (findNSupplement.getStockQuantity() < quantity) {
            throw new IllegalStateException("영양제 재고가 부족합니다.");
        }

        // 영양재 재고 차감
        findNSupplement.decreaseStockQuantity(quantity);

        // OrderOption과 Order 생성
        OrderOption orderOption = OrderOption.createOrderOption(findNSupplement, quantity);
        Order order = Order.createOrder(findUser, orderOption);

        Order saveOrder = orderRepository.save(order);

        return saveOrder.getOrderID();
    }

    // 파라미터 list or ...
    // jpa array_contains 사용 고려
    // 재고, 장바구니 삭제 확인
    @Transactional
    public Long orderCartOption(List<Long> cartOptionIds) {
        OrderOption[] orderOptions = new OrderOption[cartOptionIds.size()];

        return 0L;

    }
}
