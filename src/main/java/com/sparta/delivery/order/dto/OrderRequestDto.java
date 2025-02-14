package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequestDto {

  private String request;
  private String address;
  private String totalPrice;
  private boolean isDelivery;
  private OrderStatus status;
  private String storeId;
}
