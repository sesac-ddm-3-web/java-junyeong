package com.example.meeting_reservation.applicaiton.reservation;

import com.example.meeting_reservation.global.exception.BusinessException;

public class ReservationSlotIdCountMismatchException extends BusinessException {
    public ReservationSlotIdCountMismatchException(int expected, int actual) {
        super("잘못된 예약 id가 존재합니다. (요청한 예약 수 = " + expected + ", 조회 가능한 예약 수 = " + actual + ")");
    }
}
