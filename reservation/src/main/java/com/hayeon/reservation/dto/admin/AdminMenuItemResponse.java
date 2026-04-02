package com.hayeon.reservation.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminMenuItemResponse {
    private final String name;
    private final int quantity;
    private final int price;
}
