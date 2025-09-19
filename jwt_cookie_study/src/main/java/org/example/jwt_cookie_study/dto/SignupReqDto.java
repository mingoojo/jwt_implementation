package org.example.jwt_cookie_study.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.jwt_cookie_study.enums.UserRole;

@ToString
@Getter
@Setter
public class SignupReqDto {

    private String username;
    private String email;
    private String password;
    private String department;
    private String position;
    private UserRole role;
}
