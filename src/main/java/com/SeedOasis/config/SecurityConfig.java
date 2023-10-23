package com.SeedOasis.config;

import com.SeedOasis.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers("/", "/login/**", "/signup", "/files/notice/**", "/notice", "/files/test/**").permitAll() //누구나 이용
                .regexMatchers(HttpMethod.GET, "/notice/\\d+").permitAll()
                .mvcMatchers("/notice/**", "/notice/upload", "/admin/**").access("hasRole('ROLE_ADMIN')") //관리자
                .mvcMatchers("/css/**", "/img/**", "/js/**").permitAll() //기본
                .regexMatchers(HttpMethod.GET, "/tests/\\d+").permitAll()
                .mvcMatchers("/tests").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login/err")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }


}
