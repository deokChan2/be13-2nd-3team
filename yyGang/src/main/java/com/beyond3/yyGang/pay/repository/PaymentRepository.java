package com.beyond3.yyGang.pay.repository;

import com.beyond3.yyGang.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
