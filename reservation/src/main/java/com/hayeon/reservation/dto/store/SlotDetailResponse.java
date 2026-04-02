package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SlotDetailResponse {
    private final String slotId;
    private final LocalDate slotDate;
    private final String timeBlock;
    private final boolean isAvailable;
    private final int maxPeople;
}
