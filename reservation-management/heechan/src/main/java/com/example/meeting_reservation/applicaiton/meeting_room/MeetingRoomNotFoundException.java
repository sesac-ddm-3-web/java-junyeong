package com.example.meeting_reservation.applicaiton.meeting_room;

import com.example.meeting_reservation.global.exception.BusinessException;

public class MeetingRoomNotFoundException extends BusinessException {
    public MeetingRoomNotFoundException(String message) {
        super(message);
    }
}
