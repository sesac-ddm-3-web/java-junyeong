package spring_practice.simple_board_service.presentation;

import io.jsonwebtoken.JwtException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring_practice.simple_board_service.common.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.common.exception.InvalidPasswordException;
import spring_practice.simple_board_service.common.exception.UserAlreadyExistException;
import spring_practice.simple_board_service.common.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  record ErrorResponse(String message, String errorCode) {

  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage(), "USER_404"));
  }

  @ExceptionHandler(ArticleNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleArticleNotFoundException(ArticleNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage(), "ARTICLE_404"));
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(ex.getMessage(), "AUTH_400"));
  }


  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
      UserAlreadyExistException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse(ex.getMessage(), "USER_409"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse(ex.getMessage(), "AUTH_403"));
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponse("인증 정보가 유효하지 않습니다: " + ex.getMessage(), "AUTH_401"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {

    BindingResult bindingResult = ex.getBindingResult();
    String errorMessage;

    if (bindingResult.hasFieldErrors()) {
      FieldError fieldError = bindingResult.getFieldError();
      errorMessage = Objects.requireNonNull(fieldError).getDefaultMessage();

    } else if (bindingResult.hasGlobalErrors()) {
      errorMessage = Objects.requireNonNull(bindingResult.getGlobalError()).getDefaultMessage();

    } else {
      errorMessage = "요청 유효성 검사에 실패했습니다.";
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(errorMessage, "VALIDATION_400"));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("서버에서 예상치 못한 오류가 발생했습니다.", "SERVER_500"));
  }
}