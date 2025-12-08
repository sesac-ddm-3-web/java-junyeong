package spring_junyeong.hackathon.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring_junyeong.hackathon.presentation.auth.dto.SignupRequest;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SignupRequest> {

  // ConstraintValidator 인터페이스 구현

  // 초기화 메서드는 현재 단순 비교 로직이므로 비워둡니다.
  @Override
  public void initialize(PasswordMatch constraintAnnotation) {
    // 메시지 설정 등을 할 때 사용될 수 있습니다.
  }

  // 핵심 로직: 비밀번호와 확인 비밀번호가 같은지 비교합니다.
  @Override
  public boolean isValid(SignupRequest request, ConstraintValidatorContext context) {

    // 💡 주의: @NotBlank 검증이 먼저 통과했다고 가정합니다.
    // null 체크를 포함하여 안전하게 비교합니다.
    if (request.password() == null || request.confirmPassword() == null) {
      // @NotBlank가 처리해야 할 문제이므로, 여기서는 기본적으로 true를 반환하거나 로직을 무시
      // (다른 NotBlank 검증이 실패하면 이 isValid는 호출되지 않을 수 있습니다.)
      return true;
    }

    // 두 비밀번호가 일치하는지 비교
    return request.password().equals(request.confirmPassword());
  }
}