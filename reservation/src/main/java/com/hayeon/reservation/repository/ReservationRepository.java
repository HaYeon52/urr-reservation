package com.hayeon.reservation.repository;

import com.hayeon.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    // 전화번호로 예약 내역 최신순 조회
    List<Reservation> findByUserPhoneOrderByCreatedAtDesc(String userPhone);

    List<Reservation> findByStatusOrderByCreatedAtDesc(String status);
}
