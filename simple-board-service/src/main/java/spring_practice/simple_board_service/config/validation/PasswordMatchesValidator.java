package spring_practice.simple_board_service.config.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring_practice.simple_board_service.presentation.user.SignupRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    if (obj instanceof SignupRequest request) {
      if (request.getPassword() == null || request.getConfirmPassword() == null) {
        return false;
      }
      return request.getPassword().equals(request.getConfirmPassword());
    }
    return true;
  }
}
