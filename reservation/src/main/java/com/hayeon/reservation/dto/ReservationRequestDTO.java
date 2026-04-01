package com.hayeon.reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationRequestDTO {

    @NotBlank(message = "가게 ID는 필수입니다.")
    private String storeId;

    @NotNull(message = "예약 인원수는 필수입니다.")
    @Min(value = 1, message = "최소 1명 이상이어야 합니다.")
    private Integer headcount;

    @NotBlank(message = "예약 시간은 필수입니다.")
    private String time; 

    @NotNull(message = "메뉴 항목은 필수입니다.")
    private List<MenuItemRequest> menuItems;

    @NotNull(message = "총 금액은 필수입니다.")
    private Integer totalAmount;

    @NotNull(message = "최소 주문 금액은 필수입니다.")
    private Integer minOrderAmount;

    @Getter
    @Setter
    public static class MenuItemRequest {
        @NotBlank(message = "메뉴 ID는 필수입니다.")
        private String menuId;

        @NotBlank(message = "메뉴 이름은 필수입니다.")
        private String name;

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private Integer quantity;

        @NotNull(message = "가격은 필수입니다.")
        private Integer price;
    }
}