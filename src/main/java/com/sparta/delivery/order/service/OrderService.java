package com.sparta.delivery.order.service;

import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.repository.FoodRepository;
import com.sparta.delivery.order.dto.OrderDetailResponseDto;
import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.dto.OrderResponseDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderFood;
import com.sparta.delivery.order.repository.OrderFoodRepository;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.repository.StoreRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final StoreRepository storeRepository;

  private final OrderFoodRepository orderFoodRepository;

  private final FoodRepository foodRepository;

  public void createOrder(OrderRequestDto orderRequest, User user) {
    Store store = storeRepository.findById(UUID.fromString(orderRequest.getStoreId()))
        .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

    Food food = foodRepository.findById(UUID.fromString(String.valueOf(orderRequest.getFoodId())))
        .orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없습니다."));

    Order order = orderRepository.save(
        new Order(orderRequest, user, store, food, orderRequest.getQuantity()));

    orderFoodRepository.save(new OrderFood(food, order, orderRequest.getQuantity()));
  }

  public OrderDetailResponseDto getOrderDetail(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    Store store = storeRepository.findById(order.getStore().getId())
        .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

    List<OrderFood> orderFoodList = orderFoodRepository.findByOrderIdAndDeletedAtIsNull(orderId);

    return new OrderDetailResponseDto(order, store, user, orderFoodList);
  }

  public List<OrderResponseDto> getOrderList(User user) {
    List<Order> orderList = orderRepository.findAllByUserAndDeletedAtIsNull(user);
    List<OrderResponseDto> orderRequestDtoList = new ArrayList<>();

    for (Order order : orderList) {
      orderRequestDtoList.add(new OrderResponseDto(order, user));
    }
    return orderRequestDtoList;
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

  @Transactional
  public void deleteOrder(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    if (user.getRole().equals(UserRoleEnum.MASTER)) {
      throw new IllegalArgumentException("관리자만 주문을 삭제할 수 있습니다.");
    }

    order.updateDelete(user.getId());
  }

}
