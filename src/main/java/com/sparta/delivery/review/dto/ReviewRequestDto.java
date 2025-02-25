package com.sparta.delivery.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

  @Min(value = 1, message = "별점은 1-5점 이어야 합니다.")
  @Max(value = 5, message = "별점은 1-5점 이어야 합니다.")
  private Integer star;

  @NotBlank
  @Size(min = 10, message = "10자 이상 입력해주세요.")
  private String content;
}
