package spring_junyeong.hackathon.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuthStatus {

  Long userId;
  String token;
  Boolean isValid;
  LocalDateTime createdAt;
  LocalDateTime expiredAt;

  public AuthStatus(User user) {
    this.userId = user.getId();
    this.isValid = true;
    this.createdAt = LocalDateTime.now();
    this.expiredAt = LocalDateTime.now().plusHours(1);
    this.token = java.util.UUID.randomUUID().toString();
  }

  public void renewAccess() {
    this.isValid = true;
    this.token = java.util.UUID.randomUUID().toString();
    this.createdAt = LocalDateTime.now();
    this.expiredAt = LocalDateTime.now().plusHours(1);
  }

  public Boolean checkAccess() {
    LocalDateTime now = LocalDateTime.now();

    if (now.isBefore(this.expiredAt) && now.isAfter(this.createdAt)) {
      this.isValid = true;
    } else {
      revokeAccess();
    }

    return this.isValid;
  }

  public void revokeAccess() {
    this.isValid = false;
    this.token = null;
    this.createdAt = null;
    this.expiredAt = null;
  }

  public boolean isTokenEqual(String token) {
    return this.token.equals(token);
  }
}
