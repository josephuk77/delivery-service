package com.sparta.delivery.review.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.review.dto.ReviewRequestDto;
import com.sparta.delivery.review.dto.ReviewResponseDto;
import com.sparta.delivery.review.dto.StoreReviewsResponseDto;
import com.sparta.delivery.review.entity.Review;
import com.sparta.delivery.review.repository.ReviewRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public Page<StoreReviewsResponseDto> getReviewsByStore(
      UUID storeId,
      int page,
      int size,
      String sortedBy,
      Direction direction
  ) {
    Pageable pageable = PageRequest.of(page, size, direction, sortedBy);
    Page<Review> reviewPage = reviewRepository.findAllByStoreId(storeId, pageable);
    return reviewPage.map(review -> {
      Integer reviewCount = reviewRepository.countByStoreId(storeId);
      BigDecimal ratingAvg = reviewRepository.calculateAverageRatingByStoreId(storeId);
      return new StoreReviewsResponseDto(review.getId(), review.getUser().getId(),
          review.getOrder().getId(), review.getStar(), review.getContent(), ratingAvg, reviewCount);
    });
  }

  @Transactional
  public ReviewResponseDto createReview(ReviewRequestDto requestDto, User user) {
    validateCustomer(user);

    Order order = getOrder(requestDto);

    validateOrderUser(user, order);

    Review review = reviewRepository.save(new Review(requestDto, user));

    return new ReviewResponseDto(review);
  }

  @Transactional
  public ReviewResponseDto updateReview(UUID reviewId, ReviewRequestDto requestDto,
      User user) {
    Review review = getReview(reviewId);

    validateReviewAuthor(user, review);

    review.update(requestDto);
    reviewRepository.save(review);

    return new ReviewResponseDto(review);
  }

  @Transactional
  public void deleteReview(UUID reviewId, User user) {
    Review review = getReview(reviewId);

    validateReviewAuthor(user, review);
    review.updateDelete(user.getId());
    reviewRepository.save(review);
  }

  private Order getOrder(ReviewRequestDto requestDto) {
    return orderRepository.findById(requestDto.getOrderId()).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
  }

  private Review getReview(UUID reviewId) {
    return reviewRepository.findById(reviewId).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."));
  }

  private void validateCustomer(User user) {
    if (!user.getRole().equals(UserRoleEnum.CUSTOMER)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "고객만 리뷰를 작성할 수 있습니다.");
    }
  }

  private void validateOrderUser(User user, Order order) {
    if (!order.getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "주문한 사용자만 리뷰를 작성할 수 있습니다.");
    }
  }

  private void validateReviewAuthor(User user, Review review) {
    if (!review.getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "작성자만 수정/삭제할 수 있습니다.");
    }
  }
}
