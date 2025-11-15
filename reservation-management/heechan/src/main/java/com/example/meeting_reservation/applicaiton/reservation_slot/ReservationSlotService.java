package com.example.meeting_reservation.applicaiton.reservation_slot;

import com.example.meeting_reservation.domain.meeting_room.MeetingRoom;
import com.example.meeting_reservation.domain.meeting_room.MeetingRoomInMemoryRepository;
import com.example.meeting_reservation.domain.reservation_slot.ReservationSlot;
import com.example.meeting_reservation.domain.reservation_slot.ReservationSlotInMemoryRepository;
import com.example.meeting_reservation.presentation.reservation_slot.ReservationSlotResDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReservationSlotService {

    private final ReservationSlotInMemoryRepository reservationSlotRepository;
    private final MeetingRoomInMemoryRepository meetingRoomRepository;
    private final ModelMapper modelMapper;

    public List<ReservationSlotResDto> getReservationSlots() {
        return reservationSlotRepository.findAllReservationSlots(LocalDate.now())
                .stream().map(reservationSlot -> modelMapper.map(reservationSlot,ReservationSlotResDto.class))
                .toList();
    }

    /** 현재 시각의 다음 30분 단위부터 오늘 자정 직전까지 슬롯 생성 */
    public void createSlotsFromNowToEndOfDay(final int slotMinutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = alignToNextHalfHour(now);
        LocalDateTime endExclusive = endOfDayExclusive(now.toLocalDate());

        createSlotsForAllRooms(start, endExclusive, slotMinutes);
    }

    /** 내일 0시부터 내일 자정 직전까지 슬롯 생성 */
    public void createSlotsForTomorrow(final int slotMinutes) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime start = tomorrow.atStartOfDay();
        LocalDateTime endExclusive = endOfDayExclusive(tomorrow);

        createSlotsForAllRooms(start, endExclusive, slotMinutes);
    }

    private void createSlotsForAllRooms(LocalDateTime startInclusive, LocalDateTime endExclusive, final int slotMinutes) {
        if (!startInclusive.isBefore(endExclusive)) return;

        List<MeetingRoom> rooms = meetingRoomRepository.findAll();
        if (rooms.isEmpty()) return;

        LocalDateTime createdAt = LocalDateTime.now();

        List<ReservationSlot> allSlots = rooms.stream()
                .flatMap(room -> generateSlots(room, startInclusive, endExclusive, createdAt, slotMinutes))
                .collect(Collectors.toList());

        if (!allSlots.isEmpty()) {
            reservationSlotRepository.saveAll(allSlots);
        }
    }

    /** [start, endExclusive) 구간에서 30분 단위 슬롯 생성 */
    private Stream<ReservationSlot> generateSlots(MeetingRoom room,
                                                  LocalDateTime startInclusive,
                                                  LocalDateTime endExclusive,
                                                  LocalDateTime createdAt,
                                                  final int slotMinutes) {
        List<ReservationSlot> list = new ArrayList<>();
        LocalDateTime cur = startInclusive;

        while (cur.isBefore(endExclusive)) {
            LocalDateTime next = cur.plusMinutes(slotMinutes);
            if (next.isAfter(endExclusive)) break;
            list.add(new ReservationSlot(cur, next, createdAt, false, room));
            cur = next;
        }
        return list.stream();
    }

    /** 하루의 배타적 끝(=다음날 0시) */
    private static LocalDateTime endOfDayExclusive(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }

    /** 다음 30분 경계로 정렬 (초/나노 제거) */
    private static LocalDateTime alignToNextHalfHour(LocalDateTime dt) {
        int minute = dt.getMinute();
        int remainder = minute % 30;
        LocalDateTime base = dt.withSecond(0).withNano(0);
        if (remainder == 0) return base;
        return base.plusMinutes(30 - remainder);
    }
}
