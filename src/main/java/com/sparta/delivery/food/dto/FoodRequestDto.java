package com.sparta.delivery.food.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class FoodRequestDto {

  private String foodName;
  private String content;
  private Integer price;
  private UUID storeId;
}

