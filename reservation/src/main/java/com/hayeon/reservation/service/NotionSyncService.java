package com.hayeon.reservation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 예약 생성 후 노션 DB로 비동기 동기화. 실패해도 예약 트랜잭션에는 영향 없음.
 */
@Service
@Slf4j
public class NotionSyncService {

    @Async
    public void notifyReservationCreated(String reservationId) {
        log.debug("[NotionSync] 예약 생성 알림 (구현 예정): {}", reservationId);
    }

    @Async
    public void notifyReservationCanceled(String reservationId) {
        log.debug("[NotionSync] 예약 취소 알림 (구현 예정): {}", reservationId);
    }
}
