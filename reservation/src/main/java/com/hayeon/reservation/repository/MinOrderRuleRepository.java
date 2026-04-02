package com.hayeon.reservation.repository;

import com.hayeon.reservation.entity.MinOrderRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MinOrderRuleRepository extends JpaRepository<MinOrderRule, String> {

    List<MinOrderRule> findByStore_StoreIdOrderByMinHeadcountAsc(String storeId);

    // 특정 가게에서 인원수 구간에 맞는 최소 주문 룰 조회
    @Query("SELECT m FROM MinOrderRule m WHERE m.store.storeId = :storeId " +
           "AND :headcount BETWEEN m.minHeadcount AND m.maxHeadcount")
    Optional<MinOrderRule> findRuleByStoreAndHeadcount(
            @Param("storeId") String storeId,
            @Param("headcount") Integer headcount
    );
}

