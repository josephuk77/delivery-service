package com.sparta.delivery.food.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodWithStoreResponseDto {

  private UUID id;
  private String name;
  private String content;
  private Integer price;

}
