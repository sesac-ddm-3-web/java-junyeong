package spring_junyeong.hackathon.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordMatchValidator.class) // 👈 Validator 클래스와 연결
@Target(ElementType.TYPE) // 👈 클래스(DTO) 레벨에 붙이는 애노테이션
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {

  // 검증 실패 시 기본 메시지
  String message() default "비밀번호와 확인 비밀번호가 일치하지 않습니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}