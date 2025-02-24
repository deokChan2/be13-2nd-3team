package com.beyond3.yyGang.order;

import com.beyond3.yyGang.nsupplement.NSupplement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_option")
public class OrderOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_id")
    private Long orderOptionId;  // 주문 옵션 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_ID")
    private NSupplement nSupplement;

    private int quantity;  // 수량

    private int price;  // 가격

    protected OrderOption() {
    }

    private OrderOption(NSupplement nSupplement, int quantity) {
        this.nSupplement = nSupplement;
        this.quantity = quantity;
        this.price = calculateOrderOptionPrice();
    }

    // createOrderOption -> 명시적으로 OrderOption을 생성한다고 표현
    public static OrderOption createOrderOption(NSupplement nSupplement, int quantity) {
        return new OrderOption(nSupplement, quantity);
    }

    void setOrder(Order order) {
        this.order = order;
    }

    private int calculateOrderOptionPrice() {
        return this.getNSupplement().getPrice() * this.getQuantity();
    }


}
