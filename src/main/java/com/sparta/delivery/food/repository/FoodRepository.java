package com.sparta.delivery.food.repository;

import com.sparta.delivery.food.entity.Food;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, UUID> {

  Page<Food> findByNameOrderByCreatedAtDesc(String foodName, Pageable pageable);
}
