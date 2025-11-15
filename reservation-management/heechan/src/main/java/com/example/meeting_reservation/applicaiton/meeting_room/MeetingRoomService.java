package com.example.meeting_reservation.applicaiton.meeting_room;

import com.example.meeting_reservation.domain.meeting_room.MeetingRoom;
import com.example.meeting_reservation.domain.meeting_room.MeetingRoomInMemoryRepository;
import com.example.meeting_reservation.presentation.meeting_room.dto.MeetingRoomResDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {

    private final MeetingRoomInMemoryRepository meetingRoomInMemoryRepository;
    private final ModelMapper modelMapper;

    public List<MeetingRoomResDto> findAll() {
        return meetingRoomInMemoryRepository.findAll().stream()
                .map(meetingRoom-> modelMapper.map(meetingRoom,MeetingRoomResDto.class))
                .toList();
    }

    public MeetingRoomResDto findById(Long id){
        MeetingRoom meetingRoom = meetingRoomInMemoryRepository.findById(id)
                .orElseThrow(()-> new MeetingRoomNotFoundException("회의실이 존재하지 않습니다."));
        return modelMapper.map(meetingRoom,MeetingRoomResDto.class);
    }
}
