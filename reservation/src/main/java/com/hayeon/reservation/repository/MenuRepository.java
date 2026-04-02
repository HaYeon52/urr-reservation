package com.hayeon.reservation.repository;

import com.hayeon.reservation.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    // 특정 가게의 모든 메뉴 조회
    List<Menu> findByStore_StoreId(String storeId);
}

