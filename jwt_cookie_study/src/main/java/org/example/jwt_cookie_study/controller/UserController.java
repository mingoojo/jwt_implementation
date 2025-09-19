package org.example.jwt_cookie_study.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.jwt_cookie_study.config.CustomUserDetail;
import org.example.jwt_cookie_study.domain.User;
import org.example.jwt_cookie_study.dto.LoginReqDto;
import org.example.jwt_cookie_study.dto.LoginResDto;
import org.example.jwt_cookie_study.dto.SignupReqDto;
import org.example.jwt_cookie_study.dto.UserResDto;
import org.example.jwt_cookie_study.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/users")
    @ResponseBody
    public List<User> users() {
        return userService.findAll();
    }

    @GetMapping("/me")
    @ResponseBody
    public UserResDto user(Authentication authentication) {
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        return userService.findByUsername(user.getUsername());
    }

    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody SignupReqDto signupReqDto) {
        return userService.signup(signupReqDto);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto loginReqDto) {
        System.out.println(loginReqDto);
        return userService.login(loginReqDto);
    }

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<LoginResDto> tokenRefresh(HttpServletRequest request) {

        return userService.refreshAccessTokenByUserId(request);
    }
}
