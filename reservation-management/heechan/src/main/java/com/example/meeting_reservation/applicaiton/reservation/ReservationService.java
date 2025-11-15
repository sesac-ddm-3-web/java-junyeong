package com.example.meeting_reservation.applicaiton.reservation;

import com.example.meeting_reservation.domain.reservation.Reservation;
import com.example.meeting_reservation.domain.reservation.ReservationInMemoryRepository;
import com.example.meeting_reservation.domain.reservation_slot.ReservationSlot;
import com.example.meeting_reservation.domain.reservation_slot.ReservationSlotInMemoryRepository;
import com.example.meeting_reservation.presentation.meeting_room.dto.MeetingRoomResDto;
import com.example.meeting_reservation.presentation.reservation.dto.ReservationCreateDto;
import com.example.meeting_reservation.presentation.reservation.dto.ReservationDeleteReqDto;
import com.example.meeting_reservation.presentation.reservation.dto.ReservationResDto;

import com.example.meeting_reservation.presentation.reservation.dto.TImeSlotDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationSlotInMemoryRepository reservationSlotRepository;
    private final ReservationInMemoryRepository reservationRepository;
    private final ModelMapper modelMapper;

    public void createReservation(ReservationCreateDto reservationCreateDtos){
        reserveSlots(reservationCreateDtos.getSelectedReservationSlotIds());
        reservationSlotRepository.findReservationSlotsByIds(reservationCreateDtos.getSelectedReservationSlotIds())
                .stream().map(reservationSlot -> new Reservation(
                        reservationCreateDtos.getReserverName(),
                        reservationCreateDtos.getPassword(),
                        reservationSlot))
                .forEach(reservationRepository::save);
    }

    public List<ReservationResDto> getReservations(){
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(reservation -> new ReservationResDto(
                reservation.getId(),
                reservation.getReserverName(),
                modelMapper.map(reservation.getReservationSlot(), TImeSlotDto.class),
                modelMapper.map(reservation.getReservationSlot().getMeetingRoom(), MeetingRoomResDto.class)
        )).toList();
    }

    public void deleteReservation(ReservationDeleteReqDto reservationDeleteReqDto){
        releaseSlots(reservationDeleteReqDto.getReservationIds(), reservationDeleteReqDto.getPassword());
        reservationRepository.deleteAll(reservationDeleteReqDto.getReservationIds());
    }

    private synchronized void reserveSlots(List<Long> reservationSlotIds){
        List<ReservationSlot> reservationSlots = reservationSlotRepository.findReservationSlotsByIds(reservationSlotIds);

        if (reservationSlots.size() != reservationSlotIds.size()) {
            throw new ReservationSlotIdCountMismatchException(reservationSlotIds.size(), reservationSlots.size());
        }
        reservationSlots.forEach(ReservationSlot::isAvailable);
        reservationSlots.forEach(ReservationSlot::reserve);
    }

    private synchronized void releaseSlots(List<Long> reservationIds, String password) {
        List<Reservation> reservations = reservationRepository.findReservationsByIds(reservationIds);

        if (reservations.size() != reservationIds.size()) {
            throw new ReservationIdCountMismatchException(reservationIds.size(), reservations.size());
        }

        reservations.forEach(reservation -> reservation.validatePassword(password));
        reservations.forEach(Reservation::releaseSlot);
    }
}
