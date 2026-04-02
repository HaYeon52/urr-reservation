package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SlotTimelineItemResponse {
    private final String slotId;
    private final String timeBlock;
    private final boolean isAvailable;
    private final int maxPeople;
}
