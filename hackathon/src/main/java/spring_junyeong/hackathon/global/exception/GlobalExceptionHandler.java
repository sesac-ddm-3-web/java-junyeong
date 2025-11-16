package spring_junyeong.hackathon.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring_junyeong.hackathon.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 1. ConnectionException (도메인/인프라 오류) 처리
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
}
