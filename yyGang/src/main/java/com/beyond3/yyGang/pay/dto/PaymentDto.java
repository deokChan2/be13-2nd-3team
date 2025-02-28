package com.beyond3.yyGang.pay.dto;

import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.pay.PayStatus;
import com.beyond3.yyGang.pay.Payment;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class PaymentDto {

    private int totalPrice; // 전체 가격

    private String payMethod; // 결제 방법

    private Long orderId;   // 주문 아이디

    public Payment toEntity(Order order){
        return Payment.builder()
                .payMethod(payMethod)
                .totalPrice(totalPrice)
                .order(order)
                .build();
    }

}