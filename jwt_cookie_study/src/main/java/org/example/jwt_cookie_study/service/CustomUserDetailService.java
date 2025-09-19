package org.example.jwt_cookie_study.service;

import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.example.jwt_cookie_study.config.CustomUserDetail;
import org.example.jwt_cookie_study.domain.User;
import org.example.jwt_cookie_study.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없음");
        }

        return createUserdetails(user);
    }

    private UserDetails createUserdetails(User user) {
        Collection<? extends GrantedAuthority> authorities =
            Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));

        return new CustomUserDetail(
            user.getEmail(),
            user.getPassword(),
            authorities,
            user.getUserId()
        );
    }


}
