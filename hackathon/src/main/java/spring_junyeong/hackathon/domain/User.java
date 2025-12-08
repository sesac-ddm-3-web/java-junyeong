package spring_junyeong.hackathon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  private String profileImageUrl;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Column(nullable = false)
  private String hashedPassword;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Builder
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
