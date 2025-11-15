package com.example.meeting_reservation.presentation.reservation_slot;

import com.example.meeting_reservation.applicaiton.reservation_slot.ReservationSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationSlotController {

    private final ReservationSlotService reservationSlotService;

    @GetMapping("/reservation-slots")
    public List<ReservationSlotResDto> getReservationSlots() {
        return reservationSlotService.getReservationSlots();
    }
}
