package com.booking.member.global.config;

import com.booking.member.Auth.JwtFilter;
import com.booking.member.Auth.PrincipalOauth2UserService;
import com.booking.member.Auth.TokenProvider;
import com.booking.member.global.handler.CustomAccessDeniedHandler;
import com.booking.member.global.handler.CustomAuthenticationEntryPoint;
import com.booking.member.global.handler.CustomOAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final TokenProvider tokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("").authenticated()
                //.antMatchers("").hasAuthority(UserRole.ADMIN.name())
                .anyRequest().permitAll()
                .and()
                .httpBasic().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .frameOptions().disable()
                .and()
                .formLogin().disable()
                .logout()
                .logoutUrl("/api/auth/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .successHandler(customOAuth2LoginSuccessHandler);
        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
        http.addFilterBefore(new JwtFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}