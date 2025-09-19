package org.example.jwt_cookie_study.repository;

import org.example.jwt_cookie_study.domain.User;

public interface CustomUserRepository {

    long updateRefreshToken(long userId, String refreshToken);

    User findUserByEmail(String email);
}
