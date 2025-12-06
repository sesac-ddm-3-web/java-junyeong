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
  private String hashedPassword;
  private LocalDateTime createdAt;

  public User(String name, String email, String hashedPassword) {
    this.name = name;
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.createdAt = LocalDateTime.now();
    this.profileImageUrl = "";
  }

  public Boolean checkEmail(String email) {
    return this.email.equals(email);
  }

}
