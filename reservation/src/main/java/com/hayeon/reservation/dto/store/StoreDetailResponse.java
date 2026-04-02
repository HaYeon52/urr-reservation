package com.hayeon.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {
    private final StoreInfoResponse store;
    private final List<SlotDetailResponse> slots;
    private final List<MenuResponse> menus;
    private final List<MinOrderRuleResponse> minOrderRules;
}
