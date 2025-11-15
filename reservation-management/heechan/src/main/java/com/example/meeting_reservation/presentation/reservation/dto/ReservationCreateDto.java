package com.example.meeting_reservation.presentation.reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationCreateDto {

    @NotNull(message = "예약자 이름은 필수입니다.")
    private String reserverName;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "예약할 슬롯을 최소 1개 이상 선택해야 합니다.")
    private List<@NotNull(message = "예약 ID는 null일 수 없습니다.") Long> selectedReservationSlotIds;
}
