package com.sparta.delivery.store.repository;

import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.entity.StoreCategory;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

  @Query("SELECT s FROM Store s WHERE s.name LIKE %:keyword% AND s.deletedAt IS NULL ")
  Page<Store> findAllByName(String keyword, Pageable pageable);

  @Query("SELECT s FROM Store s WHERE s.category = :category AND s.deletedAt IS NULL")
  Page<Store> findAllByCategory(@Param("category") StoreCategory storeCategory, Pageable pageable);
}
