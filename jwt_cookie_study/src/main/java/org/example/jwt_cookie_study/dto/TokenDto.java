package org.example.jwt_cookie_study.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private String role;
}