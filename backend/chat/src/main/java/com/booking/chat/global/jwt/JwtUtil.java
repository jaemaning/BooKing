package com.booking.chat.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private static String secret;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtil.secret = secret;
    }

    public static String getLoginEmailByToken(String token) {
        //bearer
        token = token.substring(7);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(secret);

        var key = Keys.hmacShaKeyFor(keyBytes);

        // 파싱 과정에서 catch
        Jws<Claims> claims = Jwts.parserBuilder()
                                 .setSigningKey(key) // 여기에 서명 확인을 위한 키 설정
                                 .build()
                                 .parseClaimsJws(token);

        // Subject 가져오기
        return claims.getBody()
                     .getSubject();
    }

    //TODO : 구현해야함
    public static Long getMemberIdByToken(String token) {

        //bearer
        token = token.substring(7);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(secret);

        var key = Keys.hmacShaKeyFor(keyBytes);

        Integer id = Jwts.parserBuilder()
                         .setSigningKey(key)
                         .build()
                         .parseClaimsJws(token)
                         .getBody()
                         .get("id", Integer.class);

         return id.longValue();

//        return 1L;
    }
}
