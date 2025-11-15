package com.example.meeting_reservation.presentation.reservation_slot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSlotResDto {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime openedAt;
    private boolean isReserved;
    private Long meetingRoomId;
}
