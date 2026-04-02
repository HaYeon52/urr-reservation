package com.hayeon.reservation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReservationCheckResponse {
    private final String reservationId;
    private final String storeId;
    private final String slotId;
    private final String timeBlock;
    private final int headcount;
    private final int totalAmount;
    private final String status;
    private final LocalDateTime createdAt;
    private final List<ReservationMenuCheckResponse> menus;
}
