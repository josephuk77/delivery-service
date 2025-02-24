package com.sparta.delivery.order.repository;

import com.sparta.delivery.order.entity.OrderFood;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFoodRepository extends JpaRepository<OrderFood, UUID> {

  List<OrderFood> findByOrderIdAndDeletedAtIsNull(UUID orderId);

  boolean existsByOrderIdAndFoodIdAndDeletedAtIsNull(UUID id, UUID id1);
}
