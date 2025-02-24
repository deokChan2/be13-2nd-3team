package com.beyond3.yyGang.cart;

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
@Table(name = "cart_option")
public class CartOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_option_id")
    private Long cartOptionID;

    private int quantity; // 수량

    private int price;  // 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private NSupplement nSupplement;

    protected CartOption() {
    }

    private CartOption(Cart cart, NSupplement nSupplement, int quantity) {
        this.cart = cart;
        this.nSupplement = nSupplement;
        this.quantity = quantity;
        this.price = calculateCartOptionPrice();
        this.cart.addCartOption(this);
    }

    public static CartOption createCartOption(Cart cart, NSupplement nSupplement, int quantity) {
        return new CartOption(cart, nSupplement, quantity);
    }

    // 영양제 수량 증가 및 총 가격 계산
    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
        this.price = calculateCartOptionPrice();
        //this.price = this.nSupplement.getPrice() * this.quantity; // 둘 중 어느 것이 더 좋은 로직인지
    }

    // 영양제 수량 감소 및 총 가격 계산
    public void decreaseQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            this.price = calculateCartOptionPrice();
        }
    }

    // 장바구니옵션 총 가격 계산
    private int calculateCartOptionPrice() {
        return this.getNSupplement().getPrice() * this.getQuantity();
    }

}











