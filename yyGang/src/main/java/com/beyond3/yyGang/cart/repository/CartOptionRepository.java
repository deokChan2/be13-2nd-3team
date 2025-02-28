package com.beyond3.yyGang.cart.repository;

import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.cart.dto.CartOptionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CartOptionRepository extends JpaRepository<CartOption, Long> {

    @Query("select co from CartOption co where co.cart.cartId = :cartId")
    List<CartOption> findByCartId(@Param("cartId") Long cartId);

}
