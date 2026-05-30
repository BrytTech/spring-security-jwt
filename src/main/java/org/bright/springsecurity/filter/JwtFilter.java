package org.bright.springsecurity.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bright.springsecurity.entity.Role;
import org.bright.springsecurity.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
        }

        if (token != null && SecurityContextHolder.getContext().getAuthentication()==null){
            Claims claims = jwtService.verifySignatureAndExtractAllClaims(token);
            Role role = Role.valueOf("ROLE_" + claims.get("Role", String.class));

            List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>(List.of(new SimpleGrantedAuthority(role.name())));
            role.getPermissions().forEach(permission -> {
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority(permission.name()));
            });

            if (!jwtService.isTokenExpired(token)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, simpleGrantedAuthorities);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
