package com.beyond.basic.b2_board.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter {

    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        servletRequest 사용자의 요청 정보가 담겨 있음

        HttpServletRequest request = (HttpServletRequest) servletRequest; // 사용자 정보를 꺼내기 전 형변환
        String bearerToken = request.getHeader("Authorization"); // token은 Header 안에 Authorization에 들어있음
        if (bearerToken == null) {
//            token이 없는 경우 다시 filterChain으로 되돌아가는 로직
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

//        token이 있는 경우 토큰 검증 후 Authentication 객체 생성
        String token = bearerToken.substring(7);

//        token 검증 및 claims 추출 (토큰이 문제 생기면 여기서 에러 발생)
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyAt)
                .build()
                .parseClaimsJwt(token)
                .getBody();

//        GrantedAuthority - SimpleGrantedAuthority : 상속 관계
        List<GrantedAuthority> authorities = new ArrayList<>();
//        authoentication 객체를 만들 때 권한은 ROLE_ 이라는 키워드를 붙여서 만들어 주는 것이 추후 문제 발생 X
        authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));

//        claims.getSubject() : JwtTokenProvider에서 setSubject한 email
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
