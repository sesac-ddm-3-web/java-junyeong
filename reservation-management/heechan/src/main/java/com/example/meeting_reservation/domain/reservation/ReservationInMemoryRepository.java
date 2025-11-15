package com.example.meeting_reservation.domain.reservation;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationInMemoryRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void save(Reservation reservation){
        reservation.setId(idGenerator.getAndIncrement());
        reservations.put(reservation.getId(), reservation);
    }

    public List<Reservation> findAll(){
        return List.copyOf(reservations.values());
    }

    public List<Reservation> findReservationsByIds(List<Long> ids) {
        return ids.stream()
                .map(reservations::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void deleteAll(List<Long> reservationIds) {
        reservationIds.forEach(reservations::remove);
    }
}
