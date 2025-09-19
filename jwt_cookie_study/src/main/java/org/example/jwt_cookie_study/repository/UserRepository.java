package org.example.jwt_cookie_study.repository;

import org.example.jwt_cookie_study.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    
}
