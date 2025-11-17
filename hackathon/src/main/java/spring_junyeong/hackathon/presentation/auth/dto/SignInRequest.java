package spring_junyeong.hackathon.presentation.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignInRequest {

  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수 값입니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "이메일 형식이 올바르지 않습니다."
  )
  String email;

  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}$",
      message = "비밀번호는 8~20자이며, 소문자/숫자/특수문자를 각각 1자 이상 포함해야 합니다."
  )
  @NotBlank
  String password;

}
