package com.beyond3.yyGang.order.dto;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.order.OrderOption;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOptionDto {

    private long nSupplementId;     // 영양제 아이디

    private String nSupplementName; // 영양제 이름

    private int quantity;  // 수량

    private int price;    // 상품 가격

    private int totalPrice; // 전체 가격

    public OrderOption toEntity(Order order, NSupplement nSupplement) {
        return OrderOption.builder()
                .order(order)
                .nSupplement(nSupplement)
                .quantity(quantity)
                .build();
    }

}