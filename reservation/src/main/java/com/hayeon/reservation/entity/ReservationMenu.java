package com.hayeon.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation_menus", indexes = {
    @Index(name = "idx_reservation", columnList = "reservation_id"),
    @Index(name = "idx_menu", columnList = "menu_id")
})
@Getter
@Setter
public class ReservationMenu {
    @Id
    @Column(name = "id", length = 13)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_at_time", nullable = false)
    private Integer priceAtTime;
}

