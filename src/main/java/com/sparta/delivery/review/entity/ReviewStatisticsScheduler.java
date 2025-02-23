package com.sparta.delivery.review.entity;

import com.sparta.delivery.review.repository.ReviewRepository;
import com.sparta.delivery.store.repository.StoreRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewStatisticsScheduler {

  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;

  // 변경된 storeId를 추적할 Set (중복 없이 관리)
  private final Set<UUID> updatedStoreIds = new HashSet<>();

  // 리뷰 변경 시 호출하여 변경된 가게를 추적
  public void trackStoreId(UUID storeId) {
    updatedStoreIds.add(storeId);
  }

  // 스케줄러에서 변경된 가게만 통계 업데이트
  @Scheduled(cron = "0 */30 * * * *")  // 30분 간격으로 업데이트
  public void updateReviewStatistics() {
    log.info("리뷰 통계 업데이트 시작");

    if (updatedStoreIds.isEmpty()) {
      log.info("변경된 가게가 없습니다.");
      return;  // 변경된 가게가 없다면 종료
    }

    for (UUID storeId : updatedStoreIds) {
      Integer reviewCount = reviewRepository.countByStoreId(storeId);
      BigDecimal avgRating = reviewRepository.calculateAverageRatingByStoreId(storeId);

      storeRepository.updateReviewStatistics(storeId, reviewCount, avgRating);
      log.info("가게 {} 통계 업데이트 완료", storeId);
    }

    // 업데이트 후 추적 리스트 비우기
    updatedStoreIds.clear();

    log.info("리뷰 통계 업데이트 완료");
  }

}
