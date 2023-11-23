package com.booking.member.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {

    private String grantType;   // Bearer
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiresIn;
}