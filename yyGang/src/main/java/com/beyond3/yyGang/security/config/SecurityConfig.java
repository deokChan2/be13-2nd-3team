package com.beyond3.yyGang.security.config;

import com.beyond3.yyGang.security.JwtAuthenticationFilter;
import com.beyond3.yyGang.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean   // 빈 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 스프링 시큐리티가 HTTP 요청에 대한 보안 설정을 적용
        http
                .csrf(AbstractHttpConfigurer::disable)      // JWT를 사용하니 세션 사용 비활성화
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)     // 기본 HTTP 인증 방식 Basic Authentication 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sesstionManagement) ->
                        sesstionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )   // 세션 관리 정책 -> 서버가 클라이언트의 세션 상태를 유지하지 않도록 -> 매 요청마다 인증 정보를 제공하도록
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                                .requestMatchers("/user/join").permitAll() // 로그인 하지 않은 사용자 회원가입 페이지 접근 가능
                                .requestMatchers("/user/login").permitAll() // 로그인 하지 않는 사용자 login 페이지 접근 가능
                                .requestMatchers("/cart/**").permitAll()
                                .requestMatchers("/nsupplement/**").permitAll()
                                .anyRequest().authenticated());  // 위에 명시된 경로 외의 다른 모든 요청은 인증된 사용자만 접근할 수 있도록 설정
        // JWT Token 인증 방식의 미리 만들어둔 Filter를 Filter Chain에 추가

        return http.build();
    }

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        // BCryptPasswordEncoder를 기본으로 다양한 여러 암호화 방식을 추가할 수 있음
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}