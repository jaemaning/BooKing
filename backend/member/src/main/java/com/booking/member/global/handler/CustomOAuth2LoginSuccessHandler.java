package com.booking.member.global.handler;

import com.booking.member.Auth.TokenDto;
import com.booking.member.Auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 원하는 로직 수행. 예를 들면:
        // - 데이터베이스에 사용자 정보 저장
        // - 로그 생성
        // - 세션에 특정 정보 저장

        // 최종적으로 사용자를 리다이렉트할 URL 지정
        TokenDto tokenDto=tokenProvider.createToken(authentication);
//        redisTemplate.opsForValue().set()
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Success Handler : Security Context에 '{}' 인증 정보를 저장했습니다.", authentication.getName());

        // HTTP 헤더에 JWT 전달
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken","RefreshToken : "+ tokenDto.getRefreshToken());
        log.info("AccessToken={}",tokenDto.getAccessToken());
        // 원하는 URL로 리다이렉트
        redirectStrategy.sendRedirect(request, response, "/api/members/afterLogin/"+tokenDto.getAccessToken());
    }
}
