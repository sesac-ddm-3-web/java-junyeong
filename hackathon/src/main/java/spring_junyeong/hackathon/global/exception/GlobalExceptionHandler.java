package spring_junyeong.hackathon.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring_junyeong.hackathon.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConnectionException.class)
  public ResponseEntity<ErrorResponse> handleConnectionException(ConnectionException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getMessage(), "LINK_CONNECTION_FAILED") {
        });
  }

  @ExceptionHandler(FolderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleFolderNotFoundException(FolderNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(e.getMessage(), "FOLDER_NOT_FOUND"));
  }

  @ExceptionHandler(LinkNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleLinkNotFoundException(LinkNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(e.getMessage(), "LINK_NOT_FOUND"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse(e.getMessage(), "ACCESS_DENIED"));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(e.getMessage(), "USER_NOT_FOUND"));
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleUserInvalidPasswordException(
      InvalidPasswordException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getMessage(), "PASSWORD_INCORRECT"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();

    String errorMessage = String.format("%s", defaultMessage);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(errorMessage, "VALIDATION_FAILED"));
  }
}
