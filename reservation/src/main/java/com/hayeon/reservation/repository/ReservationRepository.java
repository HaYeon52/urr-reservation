package com.hayeon.reservation.repository;

// 저장소

import com.hayeon.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
