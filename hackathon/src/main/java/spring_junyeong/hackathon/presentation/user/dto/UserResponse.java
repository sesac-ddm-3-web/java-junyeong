package spring_junyeong.hackathon.presentation.user.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import spring_junyeong.hackathon.domain.User;

@Getter
public class UserResponse {

  private Long id;
  private String name;
  private String profileImageUrl;
  private String email;
  private LocalDateTime createAt;

  public UserResponse(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.profileImageUrl = user.getProfileImageUrl();
    this.email = user.getEmail();
    this.createAt = user.getCreatedAt();
  }

}
