package com.chamisnails.nailsalon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"username", "message", "status", "jwt","roles"})
public record AuthResponse(
        String username,
        String message,
        String jwt,
        Boolean status,
        List<String> roles )  {
}
