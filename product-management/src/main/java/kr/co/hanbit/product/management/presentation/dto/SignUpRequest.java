package kr.co.hanbit.product.management.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.hanbit.product.management.domain.entity.User;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효하지 않은 이메일 형식입니다.")
  private String email;

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  private String name;

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
  private String password;

  @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
  private String confirmPassword;


  public Boolean passwordMatched() {
    return this.password.equals(confirmPassword);
  }

  public User toEntity() {
    return new User(this.name, this.email, this.password);
  }
}