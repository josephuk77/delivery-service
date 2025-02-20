package com.sparta.delivery.store.dto;

import com.sparta.delivery.food.dto.FoodWithStoreResponseDto;
import com.sparta.delivery.store.entity.Store;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDetailResponseDto {

  private UUID id;
  private String category;
  private String name;
  private String content;
  private String address;
  private String phone;
  private BigDecimal ratingAvg;
  private Integer reviewCount;
  private List<FoodWithStoreResponseDto> foods;


  public StoreDetailResponseDto(Store store, BigDecimal ratingAvg, Integer reviewCount,
      List<FoodWithStoreResponseDto> foods) {
    this.id = store.getId();
    this.category = String.valueOf(store.getCategory()).toUpperCase();
    this.name = store.getName();
    this.content = store.getContent();
    this.address = store.getAddress();
    this.phone = store.getPhone();
    this.ratingAvg = ratingAvg;
    this.reviewCount = reviewCount;
    this.foods = foods;
  }
}
