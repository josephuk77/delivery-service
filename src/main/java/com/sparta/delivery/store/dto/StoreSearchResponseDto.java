package com.sparta.delivery.store.dto;

import com.sparta.delivery.store.entity.Store;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreSearchResponseDto {

  private UUID id;
  private String name;
  private BigDecimal ratingAvg;
  private Integer reviewCount;

  public StoreSearchResponseDto(Store store, BigDecimal ratingAvg, Integer reviewCount) {
    this.id = store.getId();
    this.name = store.getName();
    this.ratingAvg = ratingAvg;
    this.reviewCount = reviewCount;
  }
}
