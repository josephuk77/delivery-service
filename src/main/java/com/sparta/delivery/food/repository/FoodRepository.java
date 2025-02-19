package com.sparta.delivery.food.repository;

import com.sparta.delivery.food.entity.Food;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FoodRepository extends JpaRepository<Food, UUID> {

  Page<Food> findByNameOrderByCreatedAtDesc(String foodName, Pageable pageable);

  @Query("SELECT f From Food f WHERE f.deletedAt IS NULL AND f.isVisible = true")
  Page<Food> findByStoreId(UUID storeId, Pageable pageable);
}
