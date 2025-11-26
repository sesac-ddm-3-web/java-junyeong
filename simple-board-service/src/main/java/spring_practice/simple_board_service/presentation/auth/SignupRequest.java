package spring_practice.simple_board_service.presentation.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import spring_practice.simple_board_service.common.validation.PasswordMatches;
import spring_practice.simple_board_service.domain.auth.User;

@Getter
@PasswordMatches
public class SignupRequest {

  @NotNull(message = "이름은 필수 값입니다.")
  @Size(min = 1, max = 20, message = "이름은 1자 이상 20자 이하여야 합니다.")
  @Pattern(regexp = "^[가-힣a-zA-Z]*$", message = "이름은 한글 또는 영문만 사용할 수 있습니다.")
  private String name;

  @NotNull(message = "이메일은 필수 값입니다.")
  private String email;

  @NotNull(message = "비밀번호는 필수 값입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
  private String password;

  private String confirmPassword;

  public User toEntity() {
    return new User(this.getName(), this.getEmail(), this.getPassword());
  }
}
