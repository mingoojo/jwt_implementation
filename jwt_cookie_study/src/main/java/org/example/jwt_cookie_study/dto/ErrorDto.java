package org.example.jwt_cookie_study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ErrorDto {

    private final int status;
    private final String error;   // ex) "UNAUTHORIZED"
    private final String message; // ex) "Invalid email or password"
    private final String path;    // 요청 경로 (선택)
}
