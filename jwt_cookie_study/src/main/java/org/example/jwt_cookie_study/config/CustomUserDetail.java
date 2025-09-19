package org.example.jwt_cookie_study.config;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class CustomUserDetail extends User {

    private final long userId;

    public CustomUserDetail(String email, String password,
        Collection<? extends GrantedAuthority> authorities, long userId) {
        super(email, password, authorities);
        this.userId = userId;
    }

}
