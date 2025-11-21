package kr.co.ordermanagement.global;

import java.util.Objects;
import kr.co.ordermanagement.domain.exception.CanNotCancellableStateException;
import kr.co.ordermanagement.domain.exception.EntityNotFoundException;
import kr.co.ordermanagement.domain.exception.ErrorResponse;
import kr.co.ordermanagement.domain.exception.InsufficientStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class,
      IllegalStateException.class,
      InsufficientStockException.class})
  public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(EntityNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    String errorMessage = Objects.requireNonNull(ex.getBindingResult()
            .getFieldError())
        .getDefaultMessage();

    ErrorResponse errorResponse = new ErrorResponse(errorMessage);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonParsingExceptions(
      HttpMessageNotReadableException ex) {
    String errorMessage = "요청 본문(JSON) 형식이 잘못되었거나 필드 타입이 일치하지 않습니다.";

    ErrorResponse errorResponse = new ErrorResponse(errorMessage);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CanNotCancellableStateException.class)
  public ResponseEntity<ErrorResponse> handleCanNotCancellableState(
      CanNotCancellableStateException ex
  ) {
    ErrorResponse errorMessageDto = new ErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorMessageDto, HttpStatus.CONFLICT);
  }
}