package com.example.meeting_reservation.presentation.reservation.controller;

import com.example.meeting_reservation.applicaiton.reservation.ReservationService;
import com.example.meeting_reservation.presentation.reservation.dto.ReservationCreateDto;
import com.example.meeting_reservation.presentation.reservation.dto.ReservationDeleteReqDto;
import com.example.meeting_reservation.presentation.reservation.dto.ReservationResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public void createReservation(@Valid @RequestBody ReservationCreateDto reservationCreateDtos){
        reservationService.createReservation(reservationCreateDtos);
    }

    @GetMapping("/reservations")
    public List<ReservationResDto> getReservations(){
        return reservationService.getReservations();
    }

    @DeleteMapping("/reservations")
    public void deleteReservation(@Valid @RequestBody ReservationDeleteReqDto reservationDeleteReqDto){
        reservationService.deleteReservation(reservationDeleteReqDto);
    }
}
