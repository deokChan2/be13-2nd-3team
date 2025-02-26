package com.beyond3.yyGang.cart.repository;

import com.beyond3.yyGang.cart.CartOption;
import com.beyond3.yyGang.cart.dto.CartListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CartOptionRepository extends JpaRepository<CartOption, Long> {

    // 패치 조인 고려
    @Query("select co from CartOption co where co.cart.cartId = :cartId")
    List<CartOption> findByCartId(@Param("cartId") Long cartId);

    // 수정해야 될 거 같음
    @Query("select new com.beyond3.yyGang.cart.dto.CartListDto(co.quantity, co.price, ns.productName, ns.brand) from CartOption co join co.nSupplement ns where co.cart.cartId = :cartId")
    List<CartListDto> findCartListDtoByCartId(@Param("cartId") Long cartId);

}
