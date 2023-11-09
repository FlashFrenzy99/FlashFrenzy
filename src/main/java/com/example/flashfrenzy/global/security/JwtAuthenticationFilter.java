package com.example.flashfrenzy.global.security;

import com.example.flashfrenzy.domain.user.dto.LoginRequestDto;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.global.redis.RefreshTokenService;
import com.example.flashfrenzy.global.util.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private static final ObjectMapper mapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        setFilterProcessesUrl("/auth/users/sign-in");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        log.debug("로그인 시도");
        // 요청 본문이 비어 있는지 확인
        if (request.getContentLength() == 0) {
            throw new RuntimeException("요청 본문이 비어 있습니다.");
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        LoginRequestDto requestDto = new LoginRequestDto(username, password);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        null
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        log.debug("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();
        Long basketkey = (((UserDetailsImpl) authResult.getPrincipal()).getUser().getBasket()
                .getId());

        String token = jwtUtil.createToken(username, role, basketkey);
        String refreshToken = refreshTokenService.createRefreshToken(username, role, basketkey);
        jwtUtil.addJwtToCookie(token, refreshToken, response);

        response.sendRedirect("/");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        log.debug("로그인 실패");

        String errorMessage = "회원 정보가 일치하지 않습니다.";
        request.getSession().setAttribute("errorMessage", errorMessage);

        // 리다이렉트
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.sendRedirect("/auth/users/sign-in-page");



    }
}