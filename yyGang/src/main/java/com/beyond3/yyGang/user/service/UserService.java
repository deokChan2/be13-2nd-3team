package com.beyond3.yyGang.user.service;

import com.beyond3.yyGang.cart.Cart;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.security.JwtToken;
import com.beyond3.yyGang.security.JwtTokenProvider;
import com.beyond3.yyGang.user.dto.UserInfoDto;
import com.beyond3.yyGang.user.dto.UserJoinDTO;
import com.beyond3.yyGang.user.dto.UserModifyDto;
import com.beyond3.yyGang.user.repository.UserRepository;
import com.beyond3.yyGang.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // 생성자를 통한 의존성 주입
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Transactional
    // 회원 가입 -> 가입 후 회원ID가 반환되도록
    public void join(UserJoinDTO userJoinDTO) {

        User user = userJoinDTO.toEntity();
        // Member 중복 조회
        validateMember(user);

        if (!userJoinDTO.passwordMathcing()) {
            // 비밀번호, 비밀번호 확인이 일치하는지?
            throw new IllegalStateException("비밀번호, 비밀번호 확인이 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 회원 가입 시 회원 당 Cart 가 자동으로 생성되도록
        Cart cart = Cart.createCart(user);
        userRepository.save(user);
        cartRepository.save(cart);
    }

    // 회원 가입 시 이미 존재하는 회원인지 확인
    public void validateMember(User user){
        // Email로 중복 확인
        Optional<User> findUsers = userRepository.findByEmail(user.getEmail());
        if(findUsers.isPresent()){
            log.info("user : {}", findUsers);
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        return user.get();
    }

    @Transactional
    public void update(User user, UserModifyDto userModifyDto) {
        // 비밀번호 외의 정보를 변경할 수 있도록
        if(userModifyDto.getName() != null){
            user.setName(userModifyDto.getName());
        }
        if(userModifyDto.getRole() != null){
            user.setRole(userModifyDto.getRole());
        }
        if(userModifyDto.getAge() != null){
            user.setAge(userModifyDto.getAge());
        }
        if(userModifyDto.getGender() != null){
            user.setGender(userModifyDto.getGender());
        }
        if(userModifyDto.getPhone() != null) {
            user.setPhone(userModifyDto.getPhone());
        }
        if(userModifyDto.getAddress() != null) {
            user.setAddress(userModifyDto.getAddress());
        }

        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public List<UserInfoDto> getAllUser(){
//        return userRepository.findAll().stream()
//                .map(user -> user.toUserInfoDto())
//                .collect(Collectors.toList());
        return null;
    }

}