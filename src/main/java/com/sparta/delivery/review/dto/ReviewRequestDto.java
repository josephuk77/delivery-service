package com.sparta.delivery.review.dto;

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

  @NotBlank
  private Integer star;

  @NotBlank
  @Size(min = 10)
  private String content;
}
