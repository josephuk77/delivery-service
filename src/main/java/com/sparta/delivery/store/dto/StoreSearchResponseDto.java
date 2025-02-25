package com.sparta.delivery.store.dto;

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

}
