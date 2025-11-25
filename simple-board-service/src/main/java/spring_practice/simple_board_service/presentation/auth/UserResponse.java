package spring_practice.simple_board_service.presentation.auth;

import lombok.Getter;
import spring_practice.simple_board_service.domain.User;

@Getter
public class UserResponse {

  private Long id;
  private String name;
  private String email;

  public UserResponse(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
  }
}
