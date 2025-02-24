package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.OrderStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

  private String request;
  private String address;
  private OrderStatus status;
  private UUID storeId;
  private UUID foodId;
  private int quantity;
}
