package com.beyond3.yyGang.security;

import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtToken signIn(String username, String password) {
        // 사용자 이메일, 비밀번호를 입력 받아서 인증 토큰 생성
        Optional<User> user = userRepository.findByEmail(username);

        if(user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            // 사용자가 없거나, 패스워드가 일치하지 않음
            throw new UsernameNotFoundException("이메일 혹은 비밀번호가 잘못됐습니다.");
        }

        // 위의 과정을 통과할 경우 -> ㅇㅇ
        return new JwtToken(
                jwtTokenProvider.createAccessToken(user.get().getEmail(), user.get().getRole()),
                jwtTokenProvider.createRefreshToken(user.get().getEmail())
        );
    }

    @Transactional
    public void logout(String token){
        // token에서 access Token만 추출
        String accessToken = jwtTokenProvider.resolveToken(token);

        if(accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }

        // access Token을 BlackList에 담고

        // RefreshToken은 삭제하면 됨.
    }

}