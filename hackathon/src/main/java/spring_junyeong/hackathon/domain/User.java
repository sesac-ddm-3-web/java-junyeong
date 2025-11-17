package spring_junyeong.hackathon.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class User {

  @Setter
  private Long id;
  private String name;
  private String profileImageUrl;
  @Setter
  private String email;
  private String password;
  private LocalDateTime createdAt;

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.createdAt = LocalDateTime.now();
    this.profileImageUrl = "";
  }

  public Boolean checkEmail(String email) {
    return this.email.equals(email);
  }

  public Boolean checkPassword(String password) {
    return this.password.equals(password);
  }

}
