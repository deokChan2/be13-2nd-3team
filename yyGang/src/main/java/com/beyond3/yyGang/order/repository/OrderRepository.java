package com.beyond3.yyGang.order.repository;

import com.beyond3.yyGang.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
