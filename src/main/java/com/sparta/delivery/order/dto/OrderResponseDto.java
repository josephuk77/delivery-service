package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.OrderStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

  private UUID id;
  private String userNickname;
  private String storeName;
  private OrderStatus status;
}
