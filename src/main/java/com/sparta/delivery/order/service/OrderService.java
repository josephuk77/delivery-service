package com.sparta.delivery.order.service;

import com.sparta.delivery.aaglobal.GlobalException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final StoreRepository storeRepository;

  private final OrderFoodRepository orderFoodRepository;

  private final FoodRepository foodRepository;

  public void createOrder(OrderRequestDto requestDto, User user) {
    Store store = storeRepository.findById(UUID.fromString(requestDto.getStoreId()))
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."));

    Food food = foodRepository.findById(UUID.fromString(String.valueOf(requestDto.getFoodId())))
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "음식을 찾을 수 없습니다."));

    Order order = orderRepository.save(
        new Order(requestDto, user, store, food, requestDto.getQuantity()));

    orderFoodRepository.save(new OrderFood(food, order, requestDto.getQuantity()));
  }

  @Transactional(readOnly = true)
  public OrderDetailResponseDto getOrderDetail(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

    Store store = storeRepository.findById(order.getStore().getId())
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."));

    List<OrderFood> orderFoodList = orderFoodRepository.findByOrderIdAndDeletedAtIsNull(orderId);

    return new OrderDetailResponseDto(order, store, user, orderFoodList);
  }

  @Transactional(readOnly = true)
  public List<OrderResponseDto> getOrderList(User user) {
    List<Order> orderList = orderRepository.findAllByUserAndDeletedAtIsNull(user);
    List<OrderResponseDto> requestDtoList = new ArrayList<>();

    for (Order order : orderList) {
      requestDtoList.add(new OrderResponseDto(order, user));
    }
    return requestDtoList;
  }

  @Transactional
  public void updateOrderStatus(UUID orderId, boolean isDelivery, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

    if (!order.getUser().getId().equals(user.getId()) || user.getRole()
        .equals(UserRoleEnum.MASTER)) {
      throw new GlobalException(HttpStatus.NOT_FOUND, "본인의 주문과 관리자만 수정할 수 있습니다.");
    }

    order.updateIsDelivery(isDelivery);
  }

  @Transactional
  public void deleteOrder(UUID orderId, User user) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

    if (user.getRole().equals(UserRoleEnum.MASTER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "관리자만 주문을 삭제할 수 있습니다.");
    }

    order.updateDelete(user.getId());
  }

}
