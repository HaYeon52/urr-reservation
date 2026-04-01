package com.hayeon.reservation.controller;

import com.hayeon.reservation.dto.ReservationRequestDTO;
import com.hayeon.reservation.entity.Reservation;
import com.hayeon.reservation.entity.ReservationMenuItem;
import com.hayeon.reservation.repository.ReservationRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;

    // 생성자 주입 (마법의 볼펜 쥐여주기)
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(@Valid @RequestBody ReservationRequestDTO request) {
        try {
            // 1. 예약 메인 정보(껍데기) 생성
            Reservation reservation = new Reservation();
            reservation.setStoreId(request.getStoreId());
            reservation.setHeadcount(request.getHeadcount());
            reservation.setTime(request.getTime());
            reservation.setTotalAmount(request.getTotalAmount());
            reservation.setMinOrderAmount(request.getMinOrderAmount());

            // 2. 상세 메뉴 정보(알맹이) 변환 및 연결
            if (request.getMenuItems() != null && !request.getMenuItems().isEmpty()) {
                List<ReservationMenuItem> items = request.getMenuItems().stream()
                    .map(dtoItem -> {
                        ReservationMenuItem entityItem = new ReservationMenuItem();
                        entityItem.setMenuId(dtoItem.getMenuId());
                        entityItem.setName(dtoItem.getName());       // 프론트의 name을 엔티티의 name으로!
                        entityItem.setQuantity(dtoItem.getQuantity());
                        entityItem.setPrice(dtoItem.getPrice());
                        return entityItem;
                    })
                    .collect(Collectors.toList());

                // 예약 객체에 메뉴 리스트를 넣어줍니다.
                reservation.setMenuItems(items);
            }

            // 3. DB에 저장 (이때 RESERVATIONS와 RESERVATION_MENU_ITEMS에 동시에 들어갑니다)
            reservationRepository.save(reservation);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("예약과 상세 메뉴가 성공적으로 저장되었습니다!");

        } catch (Exception e) {
            // 에러가 나면 콘솔에 찍어서 디버깅하기 쉽게 합니다.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류: " + e.getMessage());
        }
    }
    @GetMapping("/admin/list") // 관리자 전용 데이터 호출 주소
    public List<Reservation> getAdminReservationList() {
        return reservationRepository.findAll(); 
    }
}
