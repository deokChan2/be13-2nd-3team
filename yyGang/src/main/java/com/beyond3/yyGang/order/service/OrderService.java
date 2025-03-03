package com.beyond3.yyGang.order.service;

import com.beyond3.yyGang.cart.CartRequestDto;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.order.OrderOption;
import com.beyond3.yyGang.order.dto.OrderDto;
import com.beyond3.yyGang.order.dto.OrderOptionDto;
import com.beyond3.yyGang.order.dto.OrderResultDto;
import com.beyond3.yyGang.order.repository.OrderOptionRepository;
import com.beyond3.yyGang.order.repository.OrderRepository;
import com.beyond3.yyGang.pay.repository.PersonalAccountRepository;
import com.beyond3.yyGang.security.JwtTokenProvider;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final NSupplementRepository nSupplementRepository;
    private final OrderRepository orderRepository;
    private final OrderOptionRepository orderOptionRepository;
    private final PersonalAccountRepository personalAccountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 단일 상품 주문 생성
    // orderDto : 주문할 상품 아이디와 수량이 담겨 있음
    @Transactional
    public OrderResultDto orderOne(String token, OrderDto orderDto) {
        //principal

        String userEmail = getUserEmailFromToken(token);    // 토큰에서 사용자 이메일 얻기
        User user = getUserFromEmail(userEmail);   // 사용자 받아오기

        // 주문할 상품 가져오기
        NSupplement product = nSupplementRepository.findByproductId(orderDto.getSupplement_id())
                .orElseThrow(() -> new EntityNotFoundException("해당 상품은 존재하지 않습니다."));
        log.info("nSupplement {}", product);

        // 주문이 유효한지 확인 -> 재고가 충분한지?새
        validateOrder(product, orderDto);
        log.info("validate");

        // 순서는 Order 먼저 생성 후 Order를 OrderOption에서 받는 형식
        // 먼저 user를 가지고 Order를 생성 -> OrderStatus -> PENDING -> repository에 저장
        Order order = Order.createOrder(user);
        Order saveOrder = orderRepository.save(order);
        log.info("save order {}", saveOrder);

        // 위에서 생성한 Order 객체를 가지고 OrderOption 객체 생성
        OrderOption orderOption = OrderOption.createOrderOption(saveOrder, product, orderDto.getCount());

        // 생성 orderOption을 orderOptionRepository에 저장
        OrderOption saveOrderOption = orderOptionRepository.save(orderOption);
        log.info("save orderOption {}", saveOrderOption);

        // OrderOption 을 리스트 형태로 바꿔서 OrderResultDto로 넘길 예정
        List<OrderOptionDto> orderOptionList = Arrays.asList(saveOrderOption.toDto());

        return createOrderResultDto(orderOptionList, saveOrder);
    }

    // 장바구니 상품 주문 -> 복수 상품 주문 생성
    // 장바구니 수량 변경은 x 있는 그대로 주문할 예정
    @Transactional
    public OrderResultDto orderCart(String token, CartRequestDto cartRequestDto) {

        String userEmail = getUserEmailFromToken(token);    // 토큰에서 사용자 이메일 얻기
        User user = getUserFromEmail(userEmail);   // 사용자 받아오기

        // 순서는 Order 먼저 생성 후 Order를 OrderOption에서 받는 형식
        // 먼저 user를 가지고 Order를 생성
        Order order = Order.createOrder(user);
        log.info("order {}", order);

        List<OrderOption> orderOptions = new ArrayList<>();

        // 주문할 상품 가져오기 -> 유효성 확인
        for (OrderOptionDto item : cartRequestDto.getItems()) {
            NSupplement nsup = nSupplementRepository.findById(item.getNSupplementId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 상품은 존재하지 않습니다."));

            if(nsup.getStockQuantity() < item.getQuantity()){
                throw new IllegalStateException("영양제 재고가 부족합니다.");
            }

            // 재고 있으면 orderOption 생성 -> orderOptions에 추가
            OrderOption orderOption = OrderOption.createOrderOption(order, nsup, item.getQuantity());
            orderOptions.add(orderOption);
        }

        // 생성 order를 orderRepository에 저장
        Order saveOrder = orderRepository.save(order);
        log.info("save order {}", saveOrder);

        // 생성 orderOption을 orderOptionRepository에 저장
        orderOptionRepository.saveAll(orderOptions);

        // orderOptionRepository에서 OrderId로 OrderOption들 꺼내오기
        List<OrderOptionDto> orderOptionList = orderOptions
                .stream()
                .map(OrderOption::toDto)
                .toList();

        return createOrderResultDto(orderOptionList, saveOrder);

        // 영양재 재고 차감
//        findNSupplement.decreaseStockQuantity(quantity);
    }


    // 파라미터 list or ...
    // jpa array_contains 사용 고려
    // 재고, 장바구니 삭제 확인
    @Transactional
    public Long orderCartOption(List<Long> cartOptionIds) {
        OrderOption[] orderOptions = new OrderOption[cartOptionIds.size()];

        return 0L;

    }

    // 주문 결과 정보 반환
    public OrderResultDto createOrderResultDto(List<OrderOptionDto> orderOptionList, Order order){
        int totalPrice = orderOptionList.stream().mapToInt(OrderOptionDto::getTotalPrice).sum();

        return OrderResultDto.builder()
                .orderId(order.getOrderID())
                .orderOptionDtoList(orderOptionList)
                .status(order.getOrderStatus())
                .totalPrice(totalPrice)
                .orderDate(order.getOrderDate())
                .build();
    }

    // 주문이 유효한지 확인
    private void validateOrder(NSupplement product, OrderDto orderDto) {

        // 재고 부족 시 예외처리
        if (product.getStockQuantity() < orderDto.getCount()) {
            throw new IllegalStateException("영양제 재고가 부족합니다.");
        }
    }

    // Token 정보에서 Email 추출하기
    private String getUserEmailFromToken(String token){
        String trimToken = token.substring(7).trim();

        if(!jwtTokenProvider.validateToken(trimToken)){
            // 토큰이 유효하지 않은 경우
            throw new UsernameNotFoundException("유효하지 않은 토큰입니다.");
        }
        return jwtTokenProvider.getAuthentication(trimToken).getName();
    }

    private User getUserFromEmail(String userEmail){
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if(optionalUser.isEmpty()){
            // 존재하지 않는 이메일이면?
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return optionalUser.get();
    }
}