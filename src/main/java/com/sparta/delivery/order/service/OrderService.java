package com.sparta.delivery.order.service;

import com.sparta.delivery.order.dto.OrderDetailResponseDto;
import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.repository.StoreRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import java.util.List;
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

  public OrderDetailResponseDto getOrderDetail(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    Store store = storeRepository.findById(order.getStore().getId())
        .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

    return new OrderDetailResponseDto(order, store, user);
  }

  public List<OrderRequestDto> getOrderList(User user) {
    return orderRepository.findAllByUser(user);
  }

  @Transactional
  public void updateOrderStatus(UUID orderId, boolean isDelivery, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new IllegalArgumentException("본인의 주문만 수정할 수 있습니다.");
    }

    order.updateIsDelivery(isDelivery);
  }

  public void deleteOrder(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    if (user.getRole().equals(UserRoleEnum.MASTER)) {
      throw new IllegalArgumentException("관리자만 주문을 삭제할 수 있습니다.");
    }

    orderRepository.delete(order);
  }
}
