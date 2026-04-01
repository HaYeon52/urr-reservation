package com.hayeon.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeId;

    @Column(nullable = false)
    private Integer headcount;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private Integer minOrderAmount = 0;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 상세 메뉴 리스트 (ReservationMenuItem.java 파일과 연결됨)
    // Reservation.java 파일 수정
    @ElementCollection(fetch = FetchType.EAGER) // (fetch = FetchType.EAGER) 를 추가!!
    @CollectionTable(
        name = "reservation_menu_items", 
        joinColumns = @JoinColumn(name = "reservation_id")
    )
    private List<ReservationMenuItem> menuItems = new ArrayList<>();

}