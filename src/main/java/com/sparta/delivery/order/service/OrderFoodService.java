package com.sparta.delivery.order.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.repository.FoodRepository;
import com.sparta.delivery.order.dto.OrderFoodRequestDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderFood;
import com.sparta.delivery.order.repository.OrderFoodRepository;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFoodService {

  private final OrderRepository orderRepository;
  private final OrderFoodRepository orderFoodRepository;
  private final FoodRepository foodRepository;

  public void addOrderFood(OrderFoodRequestDto requestDto, User user) {
    Order order = validateOrderExists(requestDto.getOrderId());
    Food food = validateFoodExists(requestDto.getFoodId());
    validateUserAccess(user, order);
    validateOrderModification(order);
    validateSameStore(order, food);

    orderFoodRepository.save(new OrderFood(food, order, requestDto.getQuantity()));
  }

  @Transactional
  public void updateOrderFood(UUID orderFoodId, User user, int quantity) {
    OrderFood orderFood = validateOrderFoodExists(orderFoodId);
    validateUserAccess(user, orderFood.getOrder());

    orderFood.updateQuantity(quantity);
  }

  @Transactional
  public void deleteOrderFood(UUID orderFoodId, User user) {
    OrderFood orderFood = validateOrderFoodExists(orderFoodId);
    validateUserAccess(user, orderFood.getOrder());

    orderFood.updateDelete(user.getId());
  }

  private Order validateOrderExists(UUID orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
  }

  private Food validateFoodExists(UUID foodId) {
    return foodRepository.findById(foodId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "음식을 찾을 수 없습니다."));
  }

  private void validateUserAccess(User user, Order order) {
    if (!order.getUser().getId().equals(user.getId()) && !user.getRole()
        .equals(UserRoleEnum.MASTER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "본인의 주문과 관리자만 가능합니다.");
    }
  }

  private void validateOrderModification(Order order) {
    if (order.getIsDelivery()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "완료된 주문은 수정할 수 없습니다.");
    }
  }

  private void validateSameStore(Order order, Food food) {
    if (!order.getStore().equals(food.getStore())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "주문과 음식의 가게가 다릅니다.");
    }
  }

  private OrderFood validateOrderFoodExists(UUID orderFoodId) {
    return orderFoodRepository.findById(orderFoodId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문 음식을 찾을 수 없습니다."));
  }
}
