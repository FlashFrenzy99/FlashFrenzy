package com.example.flashfrenzy.global.security;

import com.example.flashfrenzy.global.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
//authfilter,loggingfilter 대신 편리하게 사용
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {


        if (req.getRequestURI().equals("/auth/users/sign-in") ||
                req.getRequestURI().equals("/auth/users/sign-up") ||
                req.getRequestURI().equals("/") ||
                req.getRequestURI().equals("/health")) {
            log.info("Pass Authorization : " + req.getRequestURI());
            filterChain.doFilter(req, res);
            return;
        }
        //access 토큰 값
        String accessTokenValue = jwtUtil.getTokenFromRequest(req);
        //refresh 토큰 값
        String refreshTokenValue = jwtUtil.getRefreshTokenFromRequest(req);

        if (StringUtils.hasText(accessTokenValue)) {
            // JWT 토큰 substring
            accessTokenValue = jwtUtil.substringToken(accessTokenValue);

            //access토큰이 유효하면 그대로 반환, 만료되어 refresh토큰 통해 반환되면 새로운 토큰 발급
            String token = jwtUtil.validateToken(accessTokenValue, refreshTokenValue,res);
            accessTokenValue = token;

            Claims info = jwtUtil.getUserInfoFromToken(accessTokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}