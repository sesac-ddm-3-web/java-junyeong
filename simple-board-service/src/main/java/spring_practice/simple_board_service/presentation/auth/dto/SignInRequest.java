package spring_practice.simple_board_service.presentation.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignInRequest {

  @NotNull(message = "아이디는 필수 값입니다.")
  @Email(message = "아이디는 반드시 이메일 형식이어야 합니다.")
  private String email;

  @NotNull(message = "비밀번호는 필수 값입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
  private String password;
}
