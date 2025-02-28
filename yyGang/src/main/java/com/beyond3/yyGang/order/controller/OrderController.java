package com.beyond3.yyGang.order.controller;


import com.beyond3.yyGang.cart.CartRequestDto;
import com.beyond3.yyGang.order.OrderOption;
import com.beyond3.yyGang.order.dto.OrderDto;
import com.beyond3.yyGang.order.dto.OrderResultDto;
import com.beyond3.yyGang.order.service.OrderService;
import com.beyond3.yyGang.pay.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/")// 상품 하나 단일 주문 -> 주문서 생성 Controller -> 이후 결제로 넘어가기
    public ResponseEntity<OrderResultDto> createOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderDto orderDto) {

        OrderResultDto orderResultDto = orderService.orderOne(token, orderDto);

        return ResponseEntity.ok(orderResultDto);
    }

    @PostMapping("/cart")// 상품 하나 단일 주문 -> 주문서 생성 Controller -> 이후 결제로 넘어가기
    public ResponseEntity<OrderResultDto> createOrderForCart(
            @RequestHeader("Authorization") String token,
            @RequestBody CartRequestDto cartRequestDto) {

        OrderResultDto orderResultDto = orderService.orderCart(token, cartRequestDto);

        return ResponseEntity.ok(orderResultDto);
    }



}