package spring_practice.simple_board_service.presentation.auth.dto;

import lombok.Getter;

@Getter
public class AccessToken {

  private String accessToken;

  public AccessToken(String token) {
    this.accessToken = token;
  }
}
