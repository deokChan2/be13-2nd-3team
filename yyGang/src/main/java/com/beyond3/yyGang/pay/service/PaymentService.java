package com.beyond3.yyGang.pay.service;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.order.OrderStatus;
import com.beyond3.yyGang.order.dto.OrderOptionDto;
import com.beyond3.yyGang.order.dto.OrderResultDto;
import com.beyond3.yyGang.order.repository.OrderRepository;
import com.beyond3.yyGang.pay.PayStatus;
import com.beyond3.yyGang.pay.Payment;
import com.beyond3.yyGang.pay.PersonalAccount;
import com.beyond3.yyGang.pay.dto.PersonalAccountDto;
import com.beyond3.yyGang.pay.dto.PaymentDto;
import com.beyond3.yyGang.pay.dto.PaymentResultDto;
import com.beyond3.yyGang.pay.repository.PaymentRepository;
import com.beyond3.yyGang.pay.repository.PersonalAccountRepository;
import com.beyond3.yyGang.security.JwtTokenProvider;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final NSupplementRepository nSupplementRepository;
    private final PersonalAccountRepository personalAccountRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public PaymentResultDto paymentProcess(String token, PaymentDto paymentDto, OrderResultDto orderResultDto) {
        // 주문 Id를 받아서 해당 주문 상태를 변경, Payment 객체를 생성 -> PaymentResultDto를 생성하게 하면?

        String userEmail = getUserEmailFromToken(token);    // 토큰에서 사용자 이메일 얻기
        User user = getUserFromEmail(userEmail);   // 사용자 받아오기

        // orderResultDto에서 Order 객체 확인
        Order order = orderRepository.findById(orderResultDto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        // 사용자의 개인 계좌 가져오기
        PersonalAccount personalAccount = personalAccountRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("등록된 계좌가 없습니다."));

        // Payment 객체 생성
        Payment pay = paymentDto.toEntity(order);

        if(orderResultDto.getTotalPrice() > personalAccount.getBalance()) {
            pay.setStatus(PayStatus.FAIL);      // 결제 상태를 fail로 변경
            paymentRepository.save(pay);        // 결제 정보 저장
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        // orderOptionList에 들어 있는 애들 돌면서 전부 재고 수량 감소 시키기
        for(OrderOptionDto orderOptionDto : orderResultDto.getOrderOptionDtoList()){

            long nSupplementId = orderOptionDto.getNSupplementId();

            NSupplement nsup = nSupplementRepository.findById(nSupplementId)
                    .orElseThrow(() -> new EntityNotFoundException("제품이 존재하지 않습니다."));

            nsup.decreaseStockQuantity(orderOptionDto.getQuantity());
            nSupplementRepository.save(nsup);
        }

        // 계좌 잔액 감소 & 재고 수량 감소 -> 업데이트
        int newBalance = personalAccount.getBalance() - orderResultDto.getTotalPrice();
        personalAccount.setBalance(newBalance);
        personalAccountRepository.save(personalAccount);        // 계좌 잔액 감소

        // 결제 상태를 success로 변경
        pay.setStatus(PayStatus.SUCCESS);
        paymentRepository.save(pay);

        // 주문 상태를 PAID로 변경
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // 결제 성공 결과 반환
        return PaymentResultDto.builder()
                .orderId(order.getOrderID())
                .payStatus(pay.getPayStatus())
                .paidAmount(orderResultDto.getTotalPrice())
                .build();
    }


    @Transactional
    // 개인 회원 계좌 등록  -> 계좌는 인 당 하나만 만들도록
    public void personalAccountRegister(User user, PersonalAccountDto personalAccountDto) {

        validatePersonalAccount(user);
        PersonalAccount personalAccount = personalAccountDto.toEntity(user);
        personalAccountRepository.save(personalAccount);
    }

    public void validatePersonalAccount(User user){
        Optional<PersonalAccount> personalAccount = personalAccountRepository.findByUserId(user.getUserId());

        if(personalAccount.isPresent()){
            throw new IllegalStateException("이미 등록된 계좌가 존재합니다.");
        }
    }

    // Token 정보에서 Email 추출하기
    private String getUserEmailFromToken(String token){
        String trimToken = token.substring(7).trim();

        if(!jwtTokenProvider.validateToken(trimToken)){
            // 토큰이 유효하지 않은 경우
            throw new UsernameNotFoundException("유효하지 않은 토큰입니다.");
        }
        return jwtTokenProvider.getAuthentication(trimToken).getName();
    }

    private User getUserFromEmail(String userEmail){
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if(optionalUser.isEmpty()){
            // 존재하지 않는 이메일이면?
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return optionalUser.get();
    }
}
