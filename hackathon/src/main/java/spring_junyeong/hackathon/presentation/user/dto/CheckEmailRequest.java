package spring_junyeong.hackathon.presentation.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class CheckEmailRequest {

  @Getter
  @Email
  @NotBlank
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "이메일 형식이 올바르지 않습니다."
  )
  private String email;

}
