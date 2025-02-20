package com.sparta.delivery.review.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.review.dto.ReviewRequestDto;
import com.sparta.delivery.review.dto.ReviewResponseDto;
import com.sparta.delivery.review.entity.Review;
import com.sparta.delivery.review.repository.ReviewRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public ReviewResponseDto createReview(ReviewRequestDto requestDto, User user) {
    // CUSTOMER 인지 검증
    if (!user.getRole().equals(UserRoleEnum.CUSTOMER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "고객만 리뷰를 작성할 수 있습니다.");
    }

    // 주문을 했는지 검증
    Order order = orderRepository.findById(requestDto.getOrderId()).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

    // 주문한 사용자인지 검증
    if (!order.getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "주문한 사용자만 리뷰를 작성할 수 있습니다.");
    }

    Review review = reviewRepository.save(new Review(requestDto, user));

    return new ReviewResponseDto(review);
  }
}
