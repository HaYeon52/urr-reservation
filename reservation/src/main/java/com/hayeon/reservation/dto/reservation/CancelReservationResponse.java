package com.hayeon.reservation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CancelReservationResponse {
    private final String reservationId;
    private final String status;
    private final LocalDateTime canceledAt;
}
