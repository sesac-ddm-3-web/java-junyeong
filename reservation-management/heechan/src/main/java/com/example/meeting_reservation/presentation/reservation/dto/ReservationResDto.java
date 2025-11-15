package com.example.meeting_reservation.presentation.reservation.dto;

import com.example.meeting_reservation.presentation.meeting_room.dto.MeetingRoomResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResDto {
    private Long reservationId;
    private String reservationName;
    private TImeSlotDto timeSlot;
    private MeetingRoomResDto meetingRoom;
}
