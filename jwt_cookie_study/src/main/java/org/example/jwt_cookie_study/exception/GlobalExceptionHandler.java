package org.example.jwt_cookie_study.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt_cookie_study.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDto> build(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status).body(
            ErrorDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build()
        );
    }

    // 인증 관련 (스프링 시큐리티가 던지는 표준 예외들)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorDto> handleAuthBad(HttpServletRequest req, Exception e) {
        log.warn("Auth failed: {}", e.getMessage());
        // 로그인 실패라면 굳이 상세 원인 노출X (유저/비번 틀림 구분 보안상 금물)
        return build(HttpStatus.UNAUTHORIZED, "Invalid email or password", req.getRequestURI());
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ErrorDto> handleAccountExpired(HttpServletRequest req, Exception e) {
        return build(HttpStatus.UNAUTHORIZED, "Account expired", req.getRequestURI());
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ErrorDto> handleCredExpired(HttpServletRequest req, Exception e) {
        return build(HttpStatus.UNAUTHORIZED, "Credentials expired", req.getRequestURI());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorDto> handleDisabled(HttpServletRequest req, Exception e) {
        return build(HttpStatus.UNAUTHORIZED, "Account disabled", req.getRequestURI());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorDto> handleLocked(HttpServletRequest req, Exception e) {
        return build(HttpStatus.UNAUTHORIZED, "Account locked", req.getRequestURI());
    }

    // 비즈니스 로직 예외
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDto> handleIllegalState(HttpServletRequest req,
        IllegalStateException e) {
        return build(HttpStatus.CONFLICT, e.getMessage(), req.getRequestURI());
    }

    // DTO 검증 실패(@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidation(HttpServletRequest req,
        MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
            .findFirst().map(fe -> fe.getField() + " " + fe.getDefaultMessage())
            .orElse("Validation failed");
        return build(HttpStatus.BAD_REQUEST, msg, req.getRequestURI());
    }

    // 나머지
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleOther(HttpServletRequest req, Exception e) {
        log.error("Unexpected error", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error",
            req.getRequestURI());
    }
    
}
