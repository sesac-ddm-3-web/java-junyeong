package com.example.meeting_reservation.domain.reservation;

import com.example.meeting_reservation.global.exception.BusinessException;

public class InvalidReservationPasswordException extends BusinessException {
    public InvalidReservationPasswordException(String message) {
        super(message);
    }
}
