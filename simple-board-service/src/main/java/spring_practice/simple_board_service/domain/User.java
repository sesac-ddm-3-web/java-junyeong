package spring_practice.simple_board_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class User {

  @Setter private Long id;
  private String name;
  private String email;
  private String password;

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public Boolean isNameEqual(String name) {
    return name.equals(this.name);
  }

  public Boolean isEmailEqual(String email) {
    return email.equals(this.email);
  }

  public Boolean isPasswordEqual(String password) {
    return password.equals(this.password);
  }
}
