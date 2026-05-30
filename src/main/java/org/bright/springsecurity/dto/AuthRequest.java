package org.bright.springsecurity.dto;

public record AuthRequest(
        String username,
        String password,
        String role
) {
}
