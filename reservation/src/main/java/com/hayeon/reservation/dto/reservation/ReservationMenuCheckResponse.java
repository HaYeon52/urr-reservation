package com.hayeon.reservation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationMenuCheckResponse {
    private final String menuId;
    private final String name;
    private final int quantity;
    private final int priceAtTime;
}
