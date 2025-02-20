package com.sparta.delivery.order.service;

import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.repository.FoodRepository;
import com.sparta.delivery.order.dto.OrderFoodRequestDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderFood;
import com.sparta.delivery.order.repository.OrderFoodRepository;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFoodService {

  private final OrderRepository orderRepository;

  private final OrderFoodRepository orderFoodRepository;

  private final FoodRepository foodRepository;

  public void addOrderFood(OrderFoodRequestDto requestDto, User user) {
    Order order = orderRepository.findById(requestDto.getOrderId())
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    Food food = foodRepository.findById(requestDto.getFoodId())
        .orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없습니다."));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new IllegalArgumentException("본인의 주문만 수정할 수 있습니다.");
    }

    if (!order.isDelivery()) {
      throw new IllegalArgumentException("완료된 주문은 수정할 수 없습니다.");
    }

    if (order.getStore().equals(food.getStore())) {
      throw new IllegalArgumentException("주문과 음식의 가게가 다릅니다.");
    }

    orderFoodRepository.save(new OrderFood(food, order, requestDto.getQuantity()));
  }
}
