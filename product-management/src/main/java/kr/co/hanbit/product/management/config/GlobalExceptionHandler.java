package kr.co.hanbit.product.management.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kr.co.hanbit.product.management.domain.exception.EntityAlreadyExistException;
import kr.co.hanbit.product.management.domain.exception.EntityNotFoundException;
import kr.co.hanbit.product.management.domain.exception.InvalidPasswordException;
import lombok.Getter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorMessage> handleConstraintViolatedException(
      ConstraintViolationException ex
  ) {
    Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
    List<String> errors = constraintViolations.stream()
        .map(
            constraintViolation ->
                extractField(constraintViolation.getPropertyPath()) + ", "
                    + constraintViolation.getMessage()
        )
        .toList();

    ErrorMessage errorMessage = new ErrorMessage(errors);
    return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex
  ) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    List<String> errors = fieldErrors.stream()
        .map(
            fieldError ->
                fieldError.getField() + ", " + fieldError.getDefaultMessage()
        )
        .toList();

    ErrorMessage errorMessage = new ErrorMessage(errors);
    return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleEntityNotFoundExceptionException(
      EntityNotFoundException ex
  ) {
    List<String> errors = new ArrayList<>();
    errors.add(ex.getMessage());

    ErrorMessage errorMessage = new ErrorMessage(errors);
    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityAlreadyExistException.class)
  public ResponseEntity<ErrorMessage> handleEntityAlreadyExistException(
      EntityAlreadyExistException ex
  ) {
    List<String> errors = new ArrayList<>();
    errors.add(ex.getMessage());

    ErrorMessage errorMessage = new ErrorMessage(errors);
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorMessage> handleInvalidPasswordException(
      InvalidPasswordException ex
  ) {
    List<String> errors = new ArrayList<>();
    errors.add(ex.getMessage());

    ErrorMessage errorMessage = new ErrorMessage(errors);
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

  /**
   * 데이터베이스 UNIQUE 제약조건 위반 시 발생하는 예외(DuplicateKeyException) 처리 이메일 중복 시 1차 검증을 통과하고 DB에 INSERT할 때
   * 발생하며, 400 Bad Request를 반환해야 합니다.
   */
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<ErrorMessage> handleDuplicateKeyException(
      DuplicateKeyException ex
  ) {
    List<String> errors = new ArrayList<>();
    // DB 에러 메시지에서 구체적인 내용을 파싱하는 것이 좋으나, 여기서는 일반적인 메시지를 사용합니다.
    errors.add("이미 존재하는 데이터(Unique Key)가 있습니다. 이메일을 확인해주세요.");

    ErrorMessage errorMessage = new ErrorMessage(errors);
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }


  private String extractField(Path path) {
    String[] splittedArray = path.toString().split("[.]");
    int lastIndex = splittedArray.length - 1;
    return splittedArray[lastIndex];
  }

  @Getter
  public static class ErrorMessage {

    private final List<String> errors;

    public ErrorMessage(List<String> errors) {
      this.errors = errors;
    }

  }
}