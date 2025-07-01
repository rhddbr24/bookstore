package com.book.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//스프링 시큐리티
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()   // 공개할 경로
                        .anyRequest().authenticated()               // 나머지는 로그인 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")   // 커스텀 로그인 페이지가 있으면 지정
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());
        return http.build();
    }
}