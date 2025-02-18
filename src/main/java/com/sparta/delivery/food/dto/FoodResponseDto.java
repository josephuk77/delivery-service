package com.sparta.delivery.food.dto;

import com.sparta.delivery.food.entity.Food;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class FoodResponseDto {

  private UUID foodId;
  private UUID storeId;
  private String name;
  private String content;
  private Integer price;

  public FoodResponseDto(Food food) {
    this.foodId = food.getId();
    if (food.getStore() != null) {
      this.storeId = food.getStore().getId();
    }
    this.name = food.getName();
    this.content = food.getContent();
    this.price = food.getPrice();
  }
}