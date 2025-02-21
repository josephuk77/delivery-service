package com.sparta.delivery.review.dto;

import com.sparta.delivery.review.entity.Review;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

  private UUID reviewId;
  private Long userId;
  private UUID orderId;
  private Integer star;
  private String content;

  public ReviewResponseDto(Review review) {
    this.reviewId = review.getId();
    this.userId = review.getUser().getId();
    this.orderId = review.getOrder().getId();
    this.star = review.getStar();
    this.content = review.getContent();
  }
}
