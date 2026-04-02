package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreInfoResponse {
    private final String id;
    private final String name;
    private final int maxCapacity;
    private final String imageUrl;
}
