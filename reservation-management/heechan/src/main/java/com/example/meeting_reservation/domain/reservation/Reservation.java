package com.example.meeting_reservation.domain.reservation;

import com.example.meeting_reservation.domain.reservation_slot.ReservationSlot;
import lombok.Getter;

@Getter
public class Reservation {
    private Long id;
    private String reserverName;
    private String password;
    private ReservationSlot reservationSlot;

    public Reservation(String reserverName, String password, ReservationSlot reservationSlot) {
        this.reserverName = reserverName;
        this.password = password;
        this.reservationSlot = reservationSlot;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void validatePassword(String password) {
        if (password == null || !password.equals(this.password)) {
            throw new InvalidReservationPasswordException("올바르지 않은 비밀번호가 존재합니다.");
        }
    }

    public void releaseSlot() {
        this.reservationSlot.release();
    }


}
