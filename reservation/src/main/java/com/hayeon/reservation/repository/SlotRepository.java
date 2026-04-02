package com.hayeon.reservation.repository;

import com.hayeon.reservation.entity.Slot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, String> {

    // 특정 가게의 특정 날짜 슬롯 목록 조회
    List<Slot> findByStore_StoreIdAndSlotDate(String storeId, LocalDate slotDate);

    // 예약 생성 시 데이터 꼬임 방지를 위한 슬롯 행 잠금 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Slot s WHERE s.slotId = :slotId")
    Optional<Slot> findByIdWithLock(@Param("slotId") String slotId);
}

