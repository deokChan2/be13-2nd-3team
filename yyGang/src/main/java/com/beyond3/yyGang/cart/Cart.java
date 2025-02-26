package com.beyond3.yyGang.cart;

import com.beyond3.yyGang.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY) // 나중에 cascade 고려
    private List<CartOption> cartOptions = new ArrayList<>();

    protected Cart() {
    }

    private Cart(User user) {
        this.user = user;
    }

    void addCartOption(CartOption cartOption) {
        this.cartOptions.add(cartOption);
    }

    // 장바구니는 회원가입했을 때 생성되어야 할 듯
    public static Cart createCart(User user) {
        return new Cart(user);
    }

    /*public CartDto toCartDto() {
        CartDto.builder()
                .quantity(ca)
    }*/

//    @OneToMany(mappedBy = "cart")
//    private List<CartOption> cartOptions;
}
