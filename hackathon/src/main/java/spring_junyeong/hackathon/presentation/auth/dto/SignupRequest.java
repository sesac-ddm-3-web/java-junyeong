package spring_junyeong.hackathon.presentation.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequest {

  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수 값입니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "이메일 형식이 올바르지 않습니다."
  )
  private String email;

  @NotBlank(message = "비밀번호는 필수 값입니다.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}$",
      message = "비밀번호는 8~20자이며, 소문자/숫자/특수문자를 각각 1자 이상 포함해야 합니다."
  )
  private String password;

  @NotBlank(message = "이름은 필수 값입니다.")
  @Pattern(
      regexp = "^[A-Za-z가-힣]{1,10}$",
      message = "이름은 10자 이하의 한글 또는 영문만 사용할 수 있습니다."
  )
  private String name;

}
