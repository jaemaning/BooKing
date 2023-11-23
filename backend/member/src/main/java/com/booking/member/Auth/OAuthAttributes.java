package com.booking.member.Auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Builder
@Slf4j
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String id;

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao( attributes);
        }

        return ofGoogle( attributes);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .id((String) attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey("sub")
                .build();
    }


    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        log.info("kakao attributes={}",attributes);
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        String loginId=String.valueOf((Long)attributes.get("id"));

        return OAuthAttributes.builder()
                .name((String) response.get("nickname"))
                .id(loginId)
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }
}