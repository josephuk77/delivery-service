package com.sparta.delivery.review.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.review.dto.ReviewRequestDto;
import com.sparta.delivery.review.dto.ReviewResponseDto;
import com.sparta.delivery.review.dto.StoreReviewsResponseDto;
import com.sparta.delivery.review.entity.Review;
import com.sparta.delivery.review.entity.ReviewStatisticsScheduler;
import com.sparta.delivery.review.repository.ReviewRepository;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.repository.StoreRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
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
  private final StoreRepository storeRepository;
  private final ReviewStatisticsScheduler reviewStatisticsScheduler;

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

    Store store = findStore(storeId);

    Integer reviewCount = store.getReviewCount();
    BigDecimal ratingAvg = store.getRatingAvg();

    return reviewPage.map(review -> new StoreReviewsResponseDto(
        review.getId(), review.getUser().getId(),
        review.getOrder().getId(), review.getStar(), review.getContent(), ratingAvg, reviewCount));
  }

  @Transactional
  public ReviewResponseDto createReview(ReviewRequestDto requestDto, User user) {
    validateCustomer(user);
    Order order = findOrder(requestDto);

    validateOrderUser(user, order);

    // 3일 제한 검증
    validateReviewPeriod(order);

    // 중복 리뷰 체크
    validateReviewDuplicate(requestDto);

    Review review = reviewRepository.save(new Review(requestDto, user, order.getStore(), order));

    // 리뷰 생성 시 해당 가게 ID 추적
    reviewStatisticsScheduler.trackStoreId(review.getStore().getId());

    return new ReviewResponseDto(review);
  }

  @Transactional
  public ReviewResponseDto updateReview(UUID reviewId, ReviewRequestDto requestDto,
      User user) {
    Review review = findReview(reviewId);
    validateReviewAuthor(user, review);

    validateReviewPeriod(review.getOrder());

    review.update(requestDto);
    reviewRepository.save(review);

    // 리뷰 변경 시 해당 가게 ID 추적
    reviewStatisticsScheduler.trackStoreId(review.getStore().getId());

    return new ReviewResponseDto(review);
  }

  @Transactional
  public void deleteReview(UUID reviewId, User user) {
    Review review = findReview(reviewId);
    validateReviewAuthor(user, review);

    review.updateDelete(user.getId());
    reviewRepository.save(review);

    // 리뷰 삭제 시 해당 가게 ID 추적
    reviewStatisticsScheduler.trackStoreId(review.getStore().getId());
  }

  private Store findStore(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."));
  }

  private Order findOrder(ReviewRequestDto requestDto) {
    return orderRepository.findById(requestDto.getOrderId()).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
  }

  private Review findReview(UUID reviewId) {
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

  private void validateReviewPeriod(Order order) {
    LocalDateTime orderTime = order.getCreatedAt();
    LocalDateTime reviewDeadline = orderTime.plus(Duration.ofDays(3));

    if (LocalDateTime.now().isAfter(reviewDeadline)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "주문 후 3일 이내에만 리뷰를 작성/수정할 수 있습니다");
    }
  }

  private void validateReviewDuplicate(ReviewRequestDto requestDto) {
    if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 리뷰가 등록된 주문입니다.");
    }
  }

  private void validateReviewAuthor(User user, Review review) {
    if (!review.getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "작성자만 수정/삭제할 수 있습니다.");
    }
  }
}
