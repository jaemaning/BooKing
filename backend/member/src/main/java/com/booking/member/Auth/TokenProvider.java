package com.booking.member.Auth;

import com.booking.member.members.domain.UserRole;
import com.booking.member.members.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final long REFRESH_TOKEN_EXPIRE_TIME= 1000 * 60 * 60 * 24 * 7; //refresh 7일
    private final Base64.Decoder decoder = Base64.getDecoder();
    private Key key;
    private static final String AUTHORITIES_KEY = "auth";
//    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final MemberRepository memberRepository;

    public TokenProvider(
            @Value("${jwt.secret}")String secret,
            @Value("${jwt.token-validity-in-seconds}")long tokenValidityInSeconds, MemberRepository memberRepository){
        this.secret=secret;
        this.tokenValidityInMilliseconds=tokenValidityInSeconds*1000;
        this.memberRepository = memberRepository;
    }

    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes= decoder.decode(secret);
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds); // 토큰 만료 시간 설정

        //accessToken 생성
        String accessToken=Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .accessTokenExpiresIn(validity)
                .refreshToken(refreshToken)
                .build();
    }

    public Mono<TokenDto> createToken(String loginId,Integer id) {

        return memberRepository.findByLoginId(loginId)
                .flatMap(member -> {
                    long now = System.currentTimeMillis();
                    Date validity = new Date(now + tokenValidityInMilliseconds);

                    Claims claims = Jwts.claims();
                    claims.put(AUTHORITIES_KEY, member.getRole().name());
                    claims.put("id", id);

                    // accessToken 생성
                    String accessToken = Jwts.builder()
                            .setClaims(claims)
                            .setSubject(loginId)
                            .signWith(key, SignatureAlgorithm.HS512)
                            .setExpiration(validity)
                            .compact();

                    // Refresh Token 생성
                    String refreshToken = Jwts.builder()
                            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                            .signWith(key, SignatureAlgorithm.HS512)
                            .compact();

                    return Mono.just(TokenDto.builder()
                            .grantType("bearer")
                            .accessToken(accessToken)
                            .accessTokenExpiresIn(validity)
                            .refreshToken(refreshToken)
                            .build());
                });
    }

    public TokenDto createToken(String loginId) {

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds); // 토큰 만료 시간 설정
        //accessToken 생성
        String accessToken=Jwts.builder()
                .setSubject(loginId)
                .claim(AUTHORITIES_KEY, UserRole.USER.name())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .accessTokenExpiresIn(validity)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            // 파싱 과정에서 catch
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("[ERR] : 잘못된 JWT SIGN");
        } catch (ExpiredJwtException e) {
            log.error("[ERR] : 만료된 JWT TOKEN");
        } catch (UnsupportedJwtException e) {
            log.error("[ERR] : 미지원 JWT TOKEN");
        } catch (IllegalArgumentException e) {
            log.error("[ERR] : 잘못된 JWT TOKEN");
        }
        return false;
    }
}
