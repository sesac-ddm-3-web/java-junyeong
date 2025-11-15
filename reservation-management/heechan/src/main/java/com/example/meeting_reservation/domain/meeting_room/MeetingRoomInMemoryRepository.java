package com.example.meeting_reservation.domain.meeting_room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MeetingRoomInMemoryRepository{

    private final Map<Long, MeetingRoom> meetingRooms;

    public MeetingRoomInMemoryRepository(Map<Long, MeetingRoom> meetingRooms) {
        this.meetingRooms = meetingRooms;
    }

    public List<MeetingRoom> findAll() {
        return List.copyOf(meetingRooms.values());
    }

    public Optional<MeetingRoom> findById(Long id){
        return Optional.ofNullable(meetingRooms.get(id));
    }
}
