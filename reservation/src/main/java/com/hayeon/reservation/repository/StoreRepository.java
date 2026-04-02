package com.hayeon.reservation.repository;

import com.hayeon.reservation.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    List<Store> findByMaxCapacityGreaterThanEqual(Integer headcount);
}

