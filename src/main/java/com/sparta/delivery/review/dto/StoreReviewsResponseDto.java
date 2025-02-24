package com.sparta.delivery.review.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreReviewsResponseDto {

  private UUID reviewId;
  private Long userId;
  private UUID orderId;
  private Integer star;
  private String content;
  private BigDecimal ratingAvg;
  private Integer reviewCount;

}
