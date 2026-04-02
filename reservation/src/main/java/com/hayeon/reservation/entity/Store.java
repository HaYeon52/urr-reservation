package com.hayeon.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
@Getter
@Setter
public class Store {
    @Id
    @Column(name = "store_id", length = 13)
    private String storeId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

