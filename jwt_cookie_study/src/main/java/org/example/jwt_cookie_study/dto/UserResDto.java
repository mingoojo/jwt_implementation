package org.example.jwt_cookie_study.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.jwt_cookie_study.enums.UserRole;

@Getter
@Setter
@ToString
@Builder
public class UserResDto {

    private String email;
    private String username;
    private String department;
    private String position;
    private UserRole role;
    private long userId;
    private LocalDateTime createdDate;

}
