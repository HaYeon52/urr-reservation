package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreListItemResponse {
    private final String storeId;
    private final String name;
    private final int maxCapacity;
    private final String imageUrl;
    private final List<SlotTimelineItemResponse> timeline;
}
