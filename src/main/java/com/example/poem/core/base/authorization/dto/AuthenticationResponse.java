package com.example.poem.core.base.authorization.dto;

public record AuthenticationResponse(
    String token,
    String username,
    Long expirationDate
) {
}
