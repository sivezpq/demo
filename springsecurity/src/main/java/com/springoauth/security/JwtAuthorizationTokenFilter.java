package com.springoauth.security;

import com.springoauth.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;

    public JwtAuthorizationTokenFilter(@Qualifier("securityUserDetailsService") UserDetailsService userDetailsService,
                                       JwtTokenUtil jwtTokenUtil, @Value("${jwt.token}") String tokenHeader) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //1，获取token
        final String requestHeader = request.getHeader(this.tokenHeader);
        if (requestHeader == null || requestHeader.length() < 7 || !requestHeader.toLowerCase().startsWith("bearer")) {
            // 请求头中没有token，则放行，进行过滤链下一步处理
            chain.doFilter(request, response);
            return;
        }
        //2，解析token，获取username
        String authToken = requestHeader.substring(7);
        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(authToken);
        } catch (ExpiredJwtException e) {
        }
        //3，将用户信息放入SecurityContextHolder对应的上下文中，这样后续的过滤器才能获取到
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            //token失效或者SecurityContextHolder上下文中存在用户信息，则放行，进行过滤链下一步处理
            chain.doFilter(request, response);
            return;
        }
        //TODO 这里从数据库中获取，生产环境从redis中获取
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            //token有效，将用户信息放入SecurityContextHolder对应的上下文中，这样后续的过滤器才能获取到
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
