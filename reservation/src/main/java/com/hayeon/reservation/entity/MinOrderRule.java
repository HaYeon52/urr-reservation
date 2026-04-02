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
@Table(name = "min_order_rules", indexes = @Index(name = "idx_rule_lookup", columnList = "store_id, min_headcount, max_headcount"))
@Getter
@Setter
public class MinOrderRule {
    @Id
    @Column(name = "rule_id", length = 13)
    private String ruleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "min_headcount", nullable = false)
    private Integer minHeadcount;

    @Column(name = "max_headcount", nullable = false)
    private Integer maxHeadcount;

    @Column(name = "min_order_amount", nullable = false)
    private Integer minOrderAmount;
}

