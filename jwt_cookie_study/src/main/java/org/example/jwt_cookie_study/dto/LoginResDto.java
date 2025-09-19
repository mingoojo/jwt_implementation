package org.example.jwt_cookie_study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class LoginResDto {

    private String grantType;
    private String accessToken;
    private long userId;
    private String email;
    private String role;
}
