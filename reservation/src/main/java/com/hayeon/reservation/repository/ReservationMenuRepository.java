package com.hayeon.reservation.repository;

import com.hayeon.reservation.entity.ReservationMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationMenuRepository extends JpaRepository<ReservationMenu, String> {

    // 특정 예약에 포함된 메뉴 리스트 조회
    List<ReservationMenu> findByReservation_ReservationId(String reservationId);
}

