package com.sparta.delivery.order.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFoodRequestDto {

  UUID orderId;
  UUID foodId;
  int quantity;

}
