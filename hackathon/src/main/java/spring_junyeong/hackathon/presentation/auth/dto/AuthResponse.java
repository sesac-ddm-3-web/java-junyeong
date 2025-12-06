package spring_junyeong.hackathon.presentation.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse (
    @NotBlank
    String accessToken
) { }