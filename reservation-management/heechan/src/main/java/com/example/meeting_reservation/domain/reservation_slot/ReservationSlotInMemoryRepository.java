package com.example.meeting_reservation.domain.reservation_slot;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationSlotInMemoryRepository {
    private final Map<Long, ReservationSlot> slots = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void saveAll(List<ReservationSlot> reservationSlots) {
        reservationSlots.forEach(reservationSlot -> {
            reservationSlot.setId(idGenerator.getAndIncrement());
            slots.put(reservationSlot.getId(), reservationSlot);
        });
    }

    public List<ReservationSlot> findAllReservationSlots(LocalDate date) {
        return slots.values().stream()
                .filter(slot -> slot.isOnDate(date))
                .toList();
    }

    public List<ReservationSlot> findReservationSlotsByIds(List<Long> ids) {
        List<ReservationSlot> reservationSlots = new ArrayList<>();
        ids.forEach(id -> reservationSlots.add(slots.get(id)));
        return reservationSlots;
    }
}
