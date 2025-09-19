package org.example.jwt_cookie_study.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.jwt_cookie_study.config.CustomUserDetail;
import org.example.jwt_cookie_study.utils.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> WHITELIST = List.of(
        "/auth/login",
        "/auth/signup",
        "/auth/refresh"
    );
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return WHITELIST.stream().anyMatch(p -> pathMatcher.match(p, uri));
    }


    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);

            try {
                Claims userDetails = jwtProvider.parseClaims(token);

                String email = userDetails.get("email", String.class);
                Long userId = userDetails.get("userId", Number.class).longValue();

                // 여기서 필요하면 UserDetails 생성해서 SecurityContext에 저장
//                UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                        email, // principal
//                        null,                // credentials (비밀번호 필요 없음)
//                        List.of()            // 권한 넣고 싶으면 Collection<GrantedAuthority>
//                    );

                CustomUserDetail principal = new CustomUserDetail(
                    email,
                    "",          // password는 필요 없음 → 빈 문자열 or null
                    List.of(),
                    userId
                );

                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(

                        principal,
                        null,           // ✅ credentials는 null (JWT 인증은 비밀번호 안 씀)
                        principal.getAuthorities()
                    );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // 토큰이 만료된 경우
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired");
                return; // 요청 체인 중단 (다음 필터/컨트롤러 안 감)
            } catch (Exception e) {
                // 토큰 파싱 실패 (시그니처 불일치 등)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header missing");
            return;
        }

        filterChain.doFilter(request, response); //<-- 이거없으면 응답, 요청 다 안감!
    }

}
