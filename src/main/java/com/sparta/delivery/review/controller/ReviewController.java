package com.sparta.delivery.review.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.review.dto.ReviewRequestDto;
import com.sparta.delivery.review.dto.ReviewResponseDto;
import com.sparta.delivery.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private ReviewService reviewService;

  @PostMapping
  public ResponseEntity<ReviewResponseDto> createReview(
      @Valid @RequestBody ReviewRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ReviewResponseDto responseDto = reviewService.createReview(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @PatchMapping("/{reviewId}")
  public ResponseEntity<ReviewResponseDto> updateReview(
      @PathVariable UUID reviewId,
      @Valid @RequestBody ReviewRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    ReviewResponseDto responseDto = reviewService.updateReview(reviewId, requestDto,
        userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @DeleteMapping("{reviewId}")
  public ResponseEntity<?> deleteReview(
      @PathVariable UUID reviewId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    reviewService.deleteReview(reviewId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body("삭제가 완료되었습니다.");
  }
}
