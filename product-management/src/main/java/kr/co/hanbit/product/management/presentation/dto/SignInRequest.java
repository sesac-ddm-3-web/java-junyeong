package kr.co.hanbit.product.management.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "아이디는 반드시 이메일 형식이어야 합니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  private String password;
}
