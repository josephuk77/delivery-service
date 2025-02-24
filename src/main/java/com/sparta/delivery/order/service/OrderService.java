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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
    Store store = findStoreById(requestDto.getStoreId());
    Food food = findFoodById(requestDto.getFoodId());

    Order order = orderRepository.save(
        new Order(requestDto, user, store, food, requestDto.getQuantity()));
    orderFoodRepository.save(new OrderFood(food, order, requestDto.getQuantity()));
  }

  @Transactional(readOnly = true)
  public OrderDetailResponseDto getOrderDetail(UUID orderId, User user) {
    Order order = findOrderById(orderId);
    List<OrderFood> orderFoodList = orderFoodRepository.findByOrderIdAndDeletedAtIsNull(orderId);
    return new OrderDetailResponseDto(order, order.getStore(), user, orderFoodList);
  }

  @Transactional(readOnly = true)
  public List<OrderResponseDto> getOrderList(User user, Boolean isDelivery, int page, int size,
      String sortedBy, Sort.Direction direction) {
    Pageable pageable = PageRequest.of(page, size, direction, sortedBy);
    List<Order> orderList;
    if (isDelivery == null) {
      orderList = orderRepository.findAllByUserAndDeletedAtIsNull(user, pageable);
    } else {
      orderList = orderRepository.findAllByUserAndIsDeliveryAndDeletedAtIsNull(user,
          isDelivery, pageable);
    }
    List<OrderResponseDto> responseDtoList = new ArrayList<>();
    for (Order order : orderList) {
      responseDtoList.add(new OrderResponseDto(order, user));
    }
    return responseDtoList;
  }

  public Object getOwnerOrderList(User user, UUID storeId, Boolean isDelivery, int page, int size,
      String sortedBy, Direction direction) {
    Pageable pageable = PageRequest.of(page, size, direction, sortedBy);

    List<Order> orderList;
    if (isDelivery == null) {
      orderList = orderRepository.findAllByStoreIdAndDeletedAtIsNull(storeId, pageable);
    } else {
      orderList = orderRepository.findAllByStoreIdAndIsDeliveryAndDeletedAtIsNull(storeId,
          isDelivery, pageable);
    }
    List<OrderResponseDto> responseDtoList = new ArrayList<>();
    for (Order order : orderList) {
      responseDtoList.add(new OrderResponseDto(order, user));
    }
    return responseDtoList;
  }

  @Transactional
  public void updateOrderStatus(UUID orderId, boolean isDelivery, User user) {
    Order order = findOrderById(orderId);
    validateUserOrMaster(user, order);
    order.updateIsDelivery(isDelivery);
  }

  @Transactional
  public void deleteOrder(UUID orderId, User user) {
    Order order = findOrderById(orderId);
    validateMasterRole(user);
    order.updateDelete(user.getId());
  }

  private Store findStoreById(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."));
  }

  private Food findFoodById(UUID foodId) {
    return foodRepository.findById(foodId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "음식을 찾을 수 없습니다."));
  }

  private Order findOrderById(UUID orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
  }

  private void validateUserOrMaster(User user, Order order) {
    if (!order.getUser().getId().equals(user.getId()) || !user.getRole()
        .equals(UserRoleEnum.MASTER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "본인의 주문과 관리자만 수정할 수 있습니다.");
    }
  }

  private void validateMasterRole(User user) {
    if (!user.getRole().equals(UserRoleEnum.MASTER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "관리자만 주문을 삭제할 수 있습니다.");
    }
  }

  private void validateOwnerOrMaster(User user, Store store) {
    if ((!user.getRole().equals(UserRoleEnum.OWNER) && store.getUser().equals(user))
        || !user.getRole()
        .equals(UserRoleEnum.MASTER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "본인 가게와 관리자만 가게 주문을 조회할 수 있습니다.");
    }
  }

}
