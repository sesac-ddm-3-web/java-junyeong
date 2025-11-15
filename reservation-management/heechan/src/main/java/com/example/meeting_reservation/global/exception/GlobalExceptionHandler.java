package com.example.meeting_reservation.global.exception;

import com.example.meeting_reservation.applicaiton.meeting_room.MeetingRoomNotFoundException;
import com.example.meeting_reservation.applicaiton.reservation.ReservationIdCountMismatchException;
import com.example.meeting_reservation.applicaiton.reservation.ReservationSlotIdCountMismatchException;
import com.example.meeting_reservation.domain.reservation.InvalidReservationPasswordException;
import com.example.meeting_reservation.domain.reservation_slot.ReservationSlotNotAvailableException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MeetingRoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(MeetingRoomNotFoundException e) {
        return createErrorResponse("NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(ReservationIdCountMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidPasswordException(ReservationIdCountMismatchException e) {
        return createErrorResponse("RESERVATION_ID_COUNT_MISMATCH", e.getMessage());
    }

    @ExceptionHandler(ReservationSlotIdCountMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSlotNotAvailableException(ReservationSlotIdCountMismatchException e) {
        return createErrorResponse("RESERVATION_SLOT_COUNT_MISMATCH",e.getMessage());
    }

    @ExceptionHandler(ReservationSlotNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateReservationException(ReservationSlotNotAvailableException e) {
        return createErrorResponse("RESERVATION_SLOT_NOT_AVAILABLE",e.getMessage());
    }

    @ExceptionHandler(InvalidReservationPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidReservationPassword(InvalidReservationPasswordException e) {
        return createErrorResponse("INVALID_RESERVATION_PASSWORD", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ErrorResponse(
                "VALIDATION_FAILED",
                "입력값 검증에 실패했습니다.",
                errors
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodValidationException(HandlerMethodValidationException e) {
        List<String> errors = e.getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .toList();

        return new ErrorResponse(
                "VALIDATION_FAILED",
                "입력값 검증에 실패했습니다.",
                errors
        );
    }

    private ErrorResponse createErrorResponse(String code,String message) {
        return new ErrorResponse(code, message, null);
    }

    @Getter
    @AllArgsConstructor
    static class ErrorResponse {
        private String code;
        private String message;
        private List<String> errors;
    }
}