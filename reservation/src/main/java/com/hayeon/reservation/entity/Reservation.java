package com.hayeon.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_user_phone", columnList = "user_phone"),
    @Index(name = "idx_store_created", columnList = "store_id, created_at"),
    @Index(name = "idx_slot", columnList = "slot_id")
})
@Getter
@Setter
public class Reservation {
    @Id
    @Column(name = "reservation_id", length = 13)
    private String reservationId;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    @Column(name = "user_phone", nullable = false, length = 20)
    private String userPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(nullable = false)
    private Integer headcount;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private String status = "CONFIRMED";

    @Column(name = "user_note", length = 255)
    private String userNote;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}