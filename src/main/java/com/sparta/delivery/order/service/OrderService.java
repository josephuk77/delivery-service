package com.sparta.delivery.order.service;

import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.dto.OrderResponseDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.repository.StoreRepository;
import com.sparta.delivery.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final StoreRepository storeRepository;

  public void createOrder(OrderRequestDto orderRequest, User user) {
    Store store = storeRepository.findById(UUID.fromString(orderRequest.getStoreId()))
        .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

    orderRepository.save(new Order(orderRequest, user, store));
  }

  public OrderResponseDto getOrder(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    Store store = storeRepository.findById(order.getStore().getId())
        .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

    return new OrderResponseDto(order, store, user);
  }
}
