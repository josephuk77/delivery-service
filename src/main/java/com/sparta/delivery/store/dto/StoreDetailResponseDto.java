package com.sparta.delivery.store.dto;

import java.math.BigDecimal;
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

}
