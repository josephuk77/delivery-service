package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

  private String request;
  private String address;
  private String totalPrice;
  private OrderStatus status;
  private String storeId;
  private String foodId;
  private int quantity;
}
