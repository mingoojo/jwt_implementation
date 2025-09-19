package org.example.jwt_cookie_study.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.jwt_cookie_study.config.CustomUserDetail;
import org.example.jwt_cookie_study.domain.User;
import org.example.jwt_cookie_study.dto.LoginReqDto;
import org.example.jwt_cookie_study.dto.LoginResDto;
import org.example.jwt_cookie_study.dto.SignupReqDto;
import org.example.jwt_cookie_study.dto.TokenDto;
import org.example.jwt_cookie_study.dto.UserResDto;
import org.example.jwt_cookie_study.repository.UserRepository;
import org.example.jwt_cookie_study.utils.JwtProvider;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UserResDto findByUsername(String username) {
        User user = userRepository.findUserByEmail(username);

        return UserResDto.builder()
            .username(user.getUserName())
            .email(user.getEmail())
            .department(user.getDepartment())
            .position(user.getPosition())
            .role(user.getRole())
            .createdDate(user.getCreatedDate())
            .build();
    }

    public String signup(SignupReqDto signupReqDto) {
        User user = User.builder()
            .email(signupReqDto.getEmail())
            .password(passwordEncoder.encode(signupReqDto.getPassword()))
            .position(signupReqDto.getPosition())
            .department(signupReqDto.getDepartment())
            .flag('Y')
            .role(signupReqDto.getRole())
            .build();

        try {
            userRepository.save(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public ResponseEntity<LoginResDto> login(LoginReqDto loginReqDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            loginReqDto.getEmail(), loginReqDto.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto token = jwtProvider.generateToken(authentication);
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        String refreshToken = token.getRefreshToken();
        long userId = user.getUserId();

        System.out.println(user);

        long updateCnt = userRepository.updateRefreshToken(userId, refreshToken);

        if (updateCnt == 0) {
            throw new IllegalStateException("Failed to persist refresh token");
        }

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new IllegalStateException("User not found after authentication");
        }

//        LoginResDto responseBody = LoginResDto.builder()
//            .grantType("Bearer")
//            .accessToken(token.getAccessToken())
//            .userId(userId)
//            .email(user.getUsername())
//            .build();

        if (user.getAuthorities().stream().count() == 1) {
            LoginResDto responseBody = LoginResDto.builder()
                .grantType("Bearer")
                .accessToken(token.getAccessToken())
                .userId(userId)
                .role(user.getAuthorities().stream().findFirst().get().getAuthority())
                .email(user.getUsername())
                .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)       // JS 접근 차단 → XSS에 강함
                .secure(true)         // HTTPS에서만 전송
                .sameSite("None")      // 크로스사이트면 "None" + secure 필요
//            .sameSite("Lax")      // 크로스사이트면 "None" + secure 필요
                .path("/")            // 필요한 경로만 열어도 됨: "/auth"
                .maxAge(7 * 24 * 60 * 60) // 7일
                .build();

            if (updateCnt == 0) {
                throw new IllegalStateException("current_refresh_token 값 업데이트 오류");
            }

            return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(responseBody);
        } else {
            LoginResDto responseBody = LoginResDto.builder()
                .grantType("Bearer")
                .accessToken(token.getAccessToken())
                .userId(userId)
                .email(user.getUsername())
                .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)       // JS 접근 차단 → XSS에 강함
                .secure(true)         // HTTPS에서만 전송
                .sameSite("None")      // 크로스사이트면 "None" + secure 필요
//            .sameSite("Lax")      // 크로스사이트면 "None" + secure 필요
                .path("/")            // 필요한 경로만 열어도 됨: "/auth"
                .maxAge(7 * 24 * 60 * 60) // 7일
                .build();

            if (updateCnt == 0) {
                throw new IllegalStateException("current_refresh_token 값 업데이트 오류");
            }

            return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(responseBody);
        }


    }

    public ResponseEntity<LoginResDto> refreshAccessTokenByUserId(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String accessToken = auth.substring(7);

            String accessValidation = jwtProvider.validateToken(accessToken);

            if (!"Expired".equals(accessValidation)) {
                throw new SecurityException(
                    "accessToken이 만료되지 않거나 유효하지 않음: " + accessValidation);
            }

            if (cookies == null) {
                throw new IllegalStateException("refresh_token not found in cookies");
            }

            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();

                    if (refreshToken == null || refreshToken.isBlank()) {
                        throw new SecurityException("서버에 저장된 Refresh Token이 존재하지 않습니다.");
                    }

                    String refreshValidation = jwtProvider.validateToken(refreshToken);

                    if (!"Success".equals(refreshValidation)) {
                        throw new SecurityException(
                            "Refresh Token이 유효하지 않거나 만료됨: " + refreshValidation);
                    }

                    Long tokenUserId = jwtProvider.getUserIdFromRefreshToken(refreshToken);

                    Optional<User> user = userRepository.findById(tokenUserId);

                    if (user.isPresent()) { // ✅ 사용자 존재할 때만
                        String newAccessToken = jwtProvider.accessTokenReissuance(user.get());

                        LoginResDto responseBody = LoginResDto.builder()
                            .grantType("Bearer")
                            .accessToken(newAccessToken)
                            .userId(user.get().getUserId())
                            .email(user.get().getEmail())
                            .build();
                        return ResponseEntity.ok()
                            .body(responseBody);

                    } else {
                        throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
                    }

                }
            }
            throw new IllegalStateException("refresh_token not found in cookies");
        } else {
            throw new IllegalStateException("accessToken doesn't exist");
        }

    }

}
