package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MinOrderRuleResponse {
    private final int minHeadcount;
    private final int maxHeadcount;
    private final int minOrderAmount;
}
