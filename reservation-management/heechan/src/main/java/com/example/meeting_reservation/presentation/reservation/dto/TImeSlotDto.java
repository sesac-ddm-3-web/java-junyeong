package com.example.meeting_reservation.presentation.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TImeSlotDto {
    private Long slotId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
