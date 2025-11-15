package com.example.meeting_reservation.global.scheduler;

import com.example.meeting_reservation.applicaiton.reservation_slot.ReservationSlotService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationSlotScheduler {

    private final ReservationSlotService reservationSlotService;
    private final int SLOT_MINUTES = 30;

    public ReservationSlotScheduler(ReservationSlotService reservationSlotService) {
        this.reservationSlotService = reservationSlotService;
    }

    @PostConstruct
    public void init() {
        reservationSlotService.createSlotsFromNowToEndOfDay(SLOT_MINUTES);
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void createDailyReservationSlots() {
        reservationSlotService.createSlotsForTomorrow(SLOT_MINUTES);
    }
}