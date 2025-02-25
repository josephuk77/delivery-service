package com.sparta.delivery.order.repository;

import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  List<Order> findAllByUserAndDeletedAtIsNull(User user, Pageable pageable);


  List<Order> findAllByUserAndIsDeliveryAndDeletedAtIsNull(User user, Boolean isDelivery,
      Pageable pageable);

  List<Order> findAllByStoreIdAndDeletedAtIsNull(UUID storeId, Pageable pageable);

  List<Order> findAllByStoreIdAndIsDeliveryAndDeletedAtIsNull(UUID storeId, Boolean isDelivery,
      Pageable pageable);
}
