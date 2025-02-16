package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderStatus;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderResponseDto {

  private UUID id;
  private String userNickname;
  private String storeName;
  private OrderStatus status;

  public OrderResponseDto(Order order) {
    this.id = order.getId();
    this.userNickname = order.getUser().getNickname();
    this.storeName = order.getStore().getName();
    this.status = order.getStatus();
  }
}
