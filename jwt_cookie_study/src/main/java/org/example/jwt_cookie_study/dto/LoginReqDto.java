package org.example.jwt_cookie_study.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginReqDto {

    private String email;
    private String password;

}
