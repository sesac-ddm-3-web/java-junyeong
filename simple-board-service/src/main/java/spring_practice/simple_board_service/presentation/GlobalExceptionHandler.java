package spring_practice.simple_board_service.presentation;

import io.jsonwebtoken.JwtException;
import java.nio.file.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring_practice.simple_board_service.common.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.common.exception.InvalidPasswordException;
import spring_practice.simple_board_service.common.exception.UserAlreadyExistException;
import spring_practice.simple_board_service.common.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 클라이언트에게 반환할 오류 응답 구조 (record를 사용하여 간결하게 정의)
  record ErrorResponse(String message, String errorCode) {

  }

  // --- 1. 비즈니스 예외 처리 (Resource/User/Auth) ---

  /**
   * UserNotFoundException 처리 핸들러 (사용자를 찾을 수 없음) HTTP Status: 404 Not Found
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage(), "USER_404"));
  }

  /**
   * ArticleNotFoundException 처리 핸들러 (게시글을 찾을 수 없음) HTTP Status: 404 Not Found
   */
  @ExceptionHandler(ArticleNotFoundException.class) // 🚨 추가: 게시글 Not Found
  public ResponseEntity<ErrorResponse> handleArticleNotFoundException(ArticleNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage(), "ARTICLE_404"));
  }

  /**
   * InvalidPasswordException 처리 핸들러 (비밀번호 불일치) HTTP Status: 400 Bad Request
   */
  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(ex.getMessage(), "AUTH_400"));
  }

  /**
   * UserAlreadyExistException 처리 핸들러 (이메일 중복) HTTP Status: 409 Conflict
   */
  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
      UserAlreadyExistException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse(ex.getMessage(), "USER_409"));
  }

  /**
   * AccessDeniedException 처리 핸들러 (인가 실패: 권한 없음) HTTP Status: 403 Forbidden
   */
  @ExceptionHandler(AccessDeniedException.class) // 🚨 추가: 인가 실패 (권한 없음)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    // 인가 실패는 접근 권한이 없음을 의미합니다.
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse(ex.getMessage(), "AUTH_403"));
  }

  /**
   * JwtException 처리 핸들러 (토큰 만료, 잘못된 서명 등 JWT 관련 오류) HTTP Status: 401 Unauthorized 또는 400 Bad
   * Request
   */
  @ExceptionHandler(JwtException.class) // 🚨 추가: JWT 관련 오류
  public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
    // 토큰 관련 오류는 보통 인증 실패(401) 또는 요청 형식 오류(400)로 처리됩니다.
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401
        .body(new ErrorResponse("인증 정보가 유효하지 않습니다: " + ex.getMessage(), "AUTH_401"));
  }


  // --- 2. Bean Validation 실패 처리 (DTO 검증 실패) ---
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    // 첫 번째 필드 오류 메시지만 추출하여 반환
    String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400
        .body(new ErrorResponse(defaultMessage, "VALIDATION_400"));
  }

  // --- 3. 기타 일반 예외 처리 ---

  /**
   * 예상치 못한 모든 RuntimeException을 처리 (최종 방어선) HTTP Status: 500 Internal Server Error
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    // 서버 로그에는 자세히 기록하되, 클라이언트에게는 일반적인 메시지만 전달
    // 🚨 실제로는 로그를 남기는 코드를 여기에 추가해야 합니다.
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
        .body(new ErrorResponse("서버에서 예상치 못한 오류가 발생했습니다.", "SERVER_500"));
  }
}