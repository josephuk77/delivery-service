package com.sparta.delivery.review.repository;

import com.sparta.delivery.review.entity.Review;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

  @Query("SELECT COUNT(r) FROM Review r WHERE r.store.id = :storeId AND r.deletedAt IS NULL")
  Integer countByStoreId(UUID storeId);

  @Query("SELECT AVG(r.star) FROM Review r WHERE r.store.id = :storeId AND r.deletedAt IS NULL")
  BigDecimal calculateAverageRatingByStoreId(UUID storeId);
}
