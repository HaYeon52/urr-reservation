package com.hayeon.reservation.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/** admin.html 장부 화면과 필드명 호환 */
@Getter
@AllArgsConstructor
public class AdminReservationListItemResponse {
    private final String storeId;
    private final String time;
    private final int headcount;
    private final List<AdminMenuItemResponse> menuItems;
    private final int totalAmount;
}
