package com.hayeon.reservation.controller;

import com.hayeon.reservation.common.ApiResponse;
import com.hayeon.reservation.dto.store.StoreDetailResponse;
import com.hayeon.reservation.dto.store.StoreListItemResponse;
import com.hayeon.reservation.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ApiResponse<List<StoreListItemResponse>> listStores(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int headcount
    ) {
        return ApiResponse.ok(storeService.listStores(date, headcount));
    }

    @GetMapping("/{storeId}")
    public ApiResponse<StoreDetailResponse> getStore(
            @PathVariable String storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(storeService.getStoreDetail(storeId, date));
    }
}
