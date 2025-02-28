package com.beyond3.yyGang.user.controller;

import com.beyond3.yyGang.pay.service.PaymentService;
import com.beyond3.yyGang.pay.dto.PersonalAccountDto;
import com.beyond3.yyGang.security.AuthService;
import com.beyond3.yyGang.security.JwtToken;
import com.beyond3.yyGang.security.JwtTokenProvider;
import com.beyond3.yyGang.security.dto.UserLoginDto;
import com.beyond3.yyGang.user.BaseResponseDto;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.dto.PasswordModifyDto;
import com.beyond3.yyGang.user.dto.UserInfoDto;
import com.beyond3.yyGang.user.dto.UserJoinDTO;
import com.beyond3.yyGang.user.dto.UserModifyDto;
import com.beyond3.yyGang.user.repository.UserRepository;
import com.beyond3.yyGang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

//    // 로그인 페이지 -> Get 방식으로 페이지 보기
//    @GetMapping("/login")
//    public ResponseEntity<String> yyLogin(){
//        return "user/login";
//    }
//
//    // 회원 가입 페이지 -> Get 방식으로 페이지 보기
//    @GetMapping("/join")
//    public String yyJoin(){
//        return "user/join";
//    }

    // 회원 가입 처리 -> Post 방식
    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "가입 정보를 JSON으로 받아서 회원을 등록한다.")
    //@RequestBody의 JSON 형식으로 데이터를 넘길 예정
    public ResponseEntity<BaseResponseDto<User>> join(@RequestBody @Valid UserJoinDTO userJoinDTO) {

        log.info("userJoinDTO: {}", userJoinDTO);
        User user = userJoinDTO.toEntity();
        userService.join(userJoinDTO);
        log.info("user: {}", userRepository.findByEmail(userJoinDTO.getEmail()));

        return ResponseEntity.created(URI.create("/user/join/" + user.getEmail()))
                .body(new BaseResponseDto<>(HttpStatus.CREATED, user));
    }

    // JWT 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디와 패스워드를 JSON으로 받아서 로그인한다.")
    public ResponseEntity<JwtToken> signIn(
            @Valid @RequestBody UserLoginDto userLoginDto){

        log.info("request username = {}, password = {}", userLoginDto.getEmail(), userLoginDto.getPassword());
        JwtToken jwtToken = authService.signIn(userLoginDto.getEmail(), userLoginDto.getPassword());

        log.info("jwtToken: {}", jwtToken);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return ResponseEntity.ok(jwtToken);
    }


    @PostMapping("/test")
    public String test1() {
        return "success";
    }

    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<BaseResponseDto<UserInfoDto>> userInfo(Principal principal) {

        String email = principal.getName();

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        User infoUser = userService.getUserByEmail(email);
        UserInfoDto responseUserInfoDto = new UserInfoDto(infoUser);

        return ResponseEntity.created(URI.create("/user/info/" + infoUser.getEmail()))
                .body(new BaseResponseDto<>(HttpStatus.OK, responseUserInfoDto));
    }

    // 회원 탈퇴
    @PostMapping("/info/delete")
    public ResponseEntity<Void> userDelete(@RequestHeader("Authorization") String token){

        String userEmail = getUserEmailFromToken(token);

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        User infoUser = getUserFromEmail(userEmail);
        userService.delete(infoUser);
        // UserInfoDto의 경우 password를 포함하고 있음 -> 이걸 프론트 단에서 처리할지, 아예 새롭게 Dto를 생성할지 고민
        log.info("user: {}", userRepository.findByEmail(infoUser.getEmail()));

        return ResponseEntity.ok().build();
    }

    // 회원 정보 수정
    @PatchMapping("/info/modify")
    public ResponseEntity<Void> userModify(@RequestHeader("Authorization") String token, @RequestBody UserModifyDto userModifyDto){

        String userEmail = getUserEmailFromToken(token);

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        User modifyUser = getUserFromEmail(userEmail);
        userService.update(modifyUser, userModifyDto);

        return ResponseEntity.ok().build();
    }

    // 비밀번호 수정
    @PatchMapping("/info/modify-password")
    public ResponseEntity<Void> userModifyPassword(@RequestHeader("Authorization") String token, @RequestBody PasswordModifyDto passwordModifyDto){

        String userEmail = getUserEmailFromToken(token);

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        User passwordModifyUser = getUserFromEmail(userEmail);

        log.info("oldPassword : {}", passwordModifyUser.getPassword());

        if(!passwordEncoder.matches(passwordModifyDto.getOldPassword(), passwordModifyUser.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        passwordModifyUser.setPassword(passwordEncoder.encode(passwordModifyDto.getNewPassword()));
        userRepository.save(passwordModifyUser);

        log.info("newPassword : {}", userRepository.findByEmail(userEmail).get().getPassword());

        // 새로운 토큰 등록
        JwtToken jwtToken = authService.signIn(passwordModifyUser.getEmail(), passwordModifyDto.getNewPassword());
        log.info("jwtToken: {}", jwtToken);

        log.info("request username = {}, password = {}", passwordModifyUser.getEmail(), passwordModifyUser.getPassword());
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());


        return ResponseEntity.ok().build();
    }


    // 회원 목록 조회
    @GetMapping("/admin/user-list")
    public ResponseEntity<List<UserInfoDto>> adminGetUserList(){
        return ResponseEntity.ok(userService.getAllUser());
    }


    // 개인 계좌 등록
    @PostMapping("/payment")
    public ResponseEntity<Void> payment(
            @RequestHeader("Authorization") String token,
            @RequestBody PersonalAccountDto personalAccountDto)
    {
        String userEmail = getUserEmailFromToken(token);
        User user = getUserFromEmail(userEmail);
        paymentService.personalAccountRegister(user, personalAccountDto);

        return ResponseEntity.ok().build();
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