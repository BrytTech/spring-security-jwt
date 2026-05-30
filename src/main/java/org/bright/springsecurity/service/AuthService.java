package org.bright.springsecurity.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            String role = authentication
                    .getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority()
                    .replace("ROLE_", "");

            return jwtService.generateToken(username, role);
        }

        throw new RuntimeException("Authentication failed");
    }
}
