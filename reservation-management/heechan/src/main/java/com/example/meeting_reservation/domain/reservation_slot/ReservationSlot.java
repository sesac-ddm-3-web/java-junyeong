package com.example.meeting_reservation.domain.reservation_slot;

import com.example.meeting_reservation.domain.meeting_room.MeetingRoom;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReservationSlot {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime openedAt;
    private boolean isReserved;
    private MeetingRoom meetingRoom;

    public ReservationSlot(LocalDateTime startAt, LocalDateTime endAt, LocalDateTime openedAt, boolean isReserved,
                           MeetingRoom meetingRoom) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.openedAt = openedAt;
        this.isReserved = isReserved;
        this.meetingRoom = meetingRoom;
    }

    public boolean isOnDate(LocalDate date) {
        return openedAt.toLocalDate().equals(date);
    }

    public void isAvailable() {
        if(isReserved || !openedAt.isBefore(LocalDateTime.now())){
            throw new ReservationSlotNotAvailableException("예약 불가능한 슬롯이 존재합니다.");
        }
    }

    public void reserve() {
        isReserved = true;
    }

    public void release() {
        isReserved = false;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
