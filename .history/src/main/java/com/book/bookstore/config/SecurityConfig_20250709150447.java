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
                // .csrf().disable() // ❗ 테스트용: CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/loginl").permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
