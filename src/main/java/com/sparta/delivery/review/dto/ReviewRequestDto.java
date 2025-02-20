package com.sparta.delivery.review.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

  private Long userId;
  private UUID orderId;
  private Integer star;
  private String content;
}
