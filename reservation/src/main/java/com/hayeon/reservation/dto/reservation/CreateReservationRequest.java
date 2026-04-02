package com.hayeon.reservation.dto.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReservationRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String userName;

    @NotBlank(message = "단체명은 필수입니다.")
    private String groupName;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String userPhone;

    private String userNote;

    @NotBlank(message = "가게 ID는 필수입니다.")
    private String storeId;

    @NotBlank(message = "슬롯 ID는 필수입니다.")
    private String slotId;

    @NotNull(message = "인원은 필수입니다.")
    @Min(value = 1, message = "인원은 1명 이상이어야 합니다.")
    private Integer headcount;

    @NotEmpty(message = "주문 메뉴를 1개 이상 선택해 주세요.")
    @Valid
    private List<SelectedMenuRequest> selectedMenus;
}
