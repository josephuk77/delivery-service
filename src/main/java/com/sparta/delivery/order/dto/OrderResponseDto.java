package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderStatus;
import com.sparta.delivery.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

  private String userNickname;
  private String storeName;
  private OrderStatus status;

  public OrderResponseDto(Order order, User user) {
    this.userNickname = user.getNickname();
    this.storeName = order.getStore().getName();
    this.status = order.getStatus();
  }
}
