package com.sparta.delivery.review.entity;

import com.sparta.delivery.review.repository.ReviewRepository;
import com.sparta.delivery.store.repository.StoreRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewStatisticsScheduler {

  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  // Redis key 설정
  private static final String UPDATED_STORE_ID_KEY = "review:updated_storeId";
  // TTL 1시간 설정
  private static final long TTL_IN_SECONDS = 3600;

  // 리뷰 변경 시 호출하여 변경된 가게를 추적
  public void trackStoreId(UUID storeId) {
    redisTemplate.opsForSet().add(UPDATED_STORE_ID_KEY, storeId.toString());
    redisTemplate.expire(UPDATED_STORE_ID_KEY, TTL_IN_SECONDS, TimeUnit.SECONDS);
  }

  // 스케줄러에서 변경된 가게만 통계 업데이트
  @Scheduled(cron = "0 */30 * * * *")  // 30분 간격으로 업데이트
  public void updateReviewStatistics() {
    log.info("리뷰 통계 업데이트 시작");

    Optional<Set<Object>> updatedStoreIdsOptional = Optional.ofNullable(
        redisTemplate.opsForSet().members(UPDATED_STORE_ID_KEY));

    updatedStoreIdsOptional.ifPresent(updatedStoreIds -> {
      if (!updatedStoreIds.isEmpty()) {
        for (Object storeIdObj : updatedStoreIds) {
          UUID storeId = UUID.fromString(storeIdObj.toString());

          Integer reviewCount = reviewRepository.countByStoreId(storeId);
          BigDecimal avgRating = reviewRepository.calculateAverageRatingByStoreId(storeId);

          storeRepository.updateReviewStatistics(storeId, reviewCount, avgRating);
          log.info("가게 {} 통계 업데이트 완료", storeId);
        }
        // 업데이트 후 추적 리스트 비우기
        redisTemplate.delete(UPDATED_STORE_ID_KEY);
      } else {
        log.info("변경된 가게가 없습니다.");
      }
    });
  }
}