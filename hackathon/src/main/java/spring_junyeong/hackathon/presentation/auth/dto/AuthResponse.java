package spring_junyeong.hackathon.presentation.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthResponse {

  @NotBlank
  private String accessToken;

  public AuthResponse(String token) {
    this.accessToken = token;
  }
}
