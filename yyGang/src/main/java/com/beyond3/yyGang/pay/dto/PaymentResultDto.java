package com.beyond3.yyGang.pay.dto;

import com.beyond3.yyGang.pay.PayStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResultDto {

    private Long orderId;

    private PayStatus payStatus;

    private int paidAmount;
}