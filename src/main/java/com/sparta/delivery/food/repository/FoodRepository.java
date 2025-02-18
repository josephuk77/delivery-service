package com.sparta.delivery.food.repository;

import com.sparta.delivery.food.entity.Food;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FoodRepository extends JpaRepository<Food, UUID> {

  @Query("SELECT f FROM Food f WHERE f.name = :foodName AND f.deletedAt IS NULL AND f.isVisible = true ORDER BY f.createdAt DESC")
  Page<Food> findByName(String foodName, Pageable pageable);

  @Query("SELECT f FROM Food f LEFT JOIN FETCH f.store WHERE f.store.id = :storeId AND f.isVisible = true ")
  List<Food> findAllByStoreId(UUID storeId);
}
