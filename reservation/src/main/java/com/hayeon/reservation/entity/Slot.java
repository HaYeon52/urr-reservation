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

import java.time.LocalDate;

@Entity
@Table(name = "slots", indexes = {
    @Index(name = "idx_store_date", columnList = "store_id, slot_date"),
    @Index(name = "idx_date_available", columnList = "slot_date, is_available")
})
@Getter
@Setter
public class Slot {
    @Id
    @Column(name = "slot_id", length = 13)
    private String slotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "time_block", nullable = false, length = 50)
    private String timeBlock;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "max_people", nullable = false)
    private Integer maxPeople;
}

