package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponse {
    private final String menuId;
    private final String name;
    private final int price;
    private final boolean isRequired;
}
