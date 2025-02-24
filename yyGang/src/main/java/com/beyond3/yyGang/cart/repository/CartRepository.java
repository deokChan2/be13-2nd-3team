package com.beyond3.yyGang.cart.repository;

import com.beyond3.yyGang.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 페치 조인 사용 고려, 아마 사용해야할듯
    @Query("select c from Cart c where c.user.userId = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

}
