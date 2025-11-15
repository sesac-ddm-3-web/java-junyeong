package com.example.meeting_reservation.domain.reservation_slot;

import com.example.meeting_reservation.global.exception.BusinessException;

public class ReservationSlotNotAvailableException extends BusinessException {
    public ReservationSlotNotAvailableException(String message) {
        super(message);
    }
}
