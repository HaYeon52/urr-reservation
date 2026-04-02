package com.hayeon.reservation.controller;

import com.hayeon.reservation.common.ApiResponse;
import com.hayeon.reservation.dto.admin.AdminReservationListItemResponse;
import com.hayeon.reservation.dto.reservation.CancelReservationResponse;
import com.hayeon.reservation.dto.reservation.CreateReservationRequest;
import com.hayeon.reservation.dto.reservation.CreateReservationResponse;
import com.hayeon.reservation.dto.reservation.ReservationCheckResponse;
import com.hayeon.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateReservationResponse>> create(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        CreateReservationResponse data = reservationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    @GetMapping("/check")
    public ApiResponse<List<ReservationCheckResponse>> check(@RequestParam String userPhone) {
        return ApiResponse.ok(reservationService.findByPhone(userPhone));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ApiResponse<CancelReservationResponse> cancel(@PathVariable String reservationId) {
        return ApiResponse.ok(reservationService.cancel(reservationId));
    }

    /**
     * admin.html 호환: 배열 JSON 그대로 반환 (ApiResponse 래핑 없음)
     */
    @GetMapping("/admin/list")
    public List<AdminReservationListItemResponse> adminList() {
        return reservationService.getAdminListConfirmed();
    }
}
