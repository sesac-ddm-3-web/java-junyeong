package spring_practice.simple_board_service.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring_practice.simple_board_service.exception.InvalidPasswordException;
import spring_practice.simple_board_service.exception.UserAlreadyExistException;
import spring_practice.simple_board_service.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 클라이언트에게 반환할 오류 응답 구조 (record를 사용하여 간결하게 정의)
  record ErrorResponse(String message, String errorCode) {}

  // --- 1. 비즈니스 예외 처리 ---

  /** UserNotFoundException 처리 핸들러 (사용자를 찾을 수 없음) HTTP Status: 404 Not Found */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage(), "USER_404"));
  }

  /** InvalidPasswordException 처리 핸들러 (비밀번호 불일치) HTTP Status: 400 Bad Request */
  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(ex.getMessage(), "AUTH_400"));
  }

  /** UserAlreadyExistException 처리 핸들러 (이메일 중복) HTTP Status: 409 Conflict */
  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
      UserAlreadyExistException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse(ex.getMessage(), "USER_409"));
  }

  // --- 2. Bean Validation 실패 처리 (DTO 검증 실패) ---
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400
        .body(new ErrorResponse(defaultMessage, "VALIDATION_400"));
  }

  // --- 3. 기타 일반 예외 처리 ---

  /** 예상치 못한 모든 RuntimeException을 처리 (최종 방어선) HTTP Status: 500 Internal Server Error */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    // 서버 로그에는 자세히 기록하되, 클라이언트에게는 일반적인 메시지만 전달
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
        .body(new ErrorResponse("서버에서 예상치 못한 오류가 발생했습니다.", "SERVER_500"));
  }
}
