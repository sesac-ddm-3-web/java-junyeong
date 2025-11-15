package com.example.meeting_reservation.global.config;

import com.example.meeting_reservation.domain.meeting_room.MeetingRoom;
import com.example.meeting_reservation.domain.meeting_room.MeetingRoomInMemoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public MeetingRoomInMemoryRepository meetingRoomInMemoryRepository() {
        Map<Long, MeetingRoom> meetingRooms = createMeetingRooms();
        return new MeetingRoomInMemoryRepository(meetingRooms);
    }

    private Map<Long, MeetingRoom> createMeetingRooms() {
        return Map.of(
                1L, new MeetingRoom(1L, "회의실 A"),
                2L, new MeetingRoom(2L, "회의실 B"),
                3L, new MeetingRoom(3L, "회의실 C")
        );
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }
}
