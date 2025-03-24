package com.beyond3.yyGang.cart.repository;

import com.beyond3.yyGang.cart.Cart;
import com.beyond3.yyGang.cart.CartOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.user.userId = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

    @Query("select c from Cart c where c.user.email = :userEmail")
    Optional<Cart> findByUserEmail(@Param("userEmail") String email);

    @Query("select c from Cart c join fetch c.cartOptions co join co.nSupplement ns where c.user.email = :userEmail")
    Optional<Cart> findByUserEmailWithCartOptions(@Param("userEmail") String email);

    @Query("select co " +
            "from CartOption co " +
            "join fetch co.cart c " +
            "where c.user.email = :userEmail")
    Page<CartOption> findCartOptionByUserEmail(@Param("userEmail") String email, Pageable pageable);


}
