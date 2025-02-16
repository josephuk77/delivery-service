package com.sparta.delivery.order.repository;

import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  List<OrderRequestDto> findAllByUser(User user);
}
