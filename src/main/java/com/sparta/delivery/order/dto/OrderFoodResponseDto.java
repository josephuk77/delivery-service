package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.OrderFood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFoodResponseDto {

  private String foodName;
  private int price;
  private int quantity;
  private int totalPrice;

  public OrderFoodResponseDto(OrderFood orderFood) {
    this.foodName = orderFood.getFood().getName();
    this.price = orderFood.getFood().getPrice();
    this.quantity = orderFood.getQuantity();
    this.totalPrice = price * quantity;
  }
}
