package com.example.meeting_reservation.presentation.meeting_room.controller;

import com.example.meeting_reservation.applicaiton.meeting_room.MeetingRoomService;
import com.example.meeting_reservation.presentation.meeting_room.dto.MeetingRoomResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    @GetMapping("/meeting-rooms")
    public List<MeetingRoomResDto> getMeetingRooms() {
        return meetingRoomService.findAll();
    }
}