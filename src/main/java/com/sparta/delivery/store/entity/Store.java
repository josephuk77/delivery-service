package com.sparta.delivery.store.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.store.dto.StoreRequestDto;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_stores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_id", updatable = false, nullable = false)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StoreCategory category;

  @Column(name = "store_name", nullable = false, unique = true)
  private String name;

  @Column(name = "store_content", nullable = false)
  private String content;

  @Column(name = "store_address", nullable = false)
  private String address;

  @Column(nullable = false)
  private String phone;

  @Column
  private Integer reviewCount = 0;

  @Column(precision = 3, scale = 1)
  private BigDecimal ratingAvg = BigDecimal.ZERO;

  @Column(name = "last_review_updated_at")
  private LocalDateTime lastReviewUpdatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Store(StoreRequestDto requestDto, User user) {
    this.category = StoreCategory.fromString(requestDto.getCategory());
    this.name = requestDto.getName();
    this.content = requestDto.getContent();
    this.address = requestDto.getAddress();
    this.phone = requestDto.getPhone();
    this.user = user;
  }

  // 가게 정보가 수정된 경우에만 updatedAt 갱신
  @PreUpdate
  public void preUpdate() {
    if (!this.getCreatedAt().equals(this.getUpdatedAt())) {
      this.modifyUpdatedAt(LocalDateTime.now());
    }
  }

  public void update(StoreRequestDto requestDto) {
    this.category = StoreCategory.fromString(requestDto.getCategory());
    this.name = requestDto.getName();
    this.content = requestDto.getContent();
    this.address = requestDto.getAddress();
    this.phone = requestDto.getPhone();
  }

  public void updateReviewStats(Integer reviewCount, BigDecimal ratingAvg) {
    this.reviewCount = reviewCount;
    this.ratingAvg = ratingAvg;

    // 리뷰 통계 갱신 시마다 lastReviewUpdatedAt 갱신
    updateLastReviewUpdatedAt();
  }

  private void updateLastReviewUpdatedAt() {
    this.lastReviewUpdatedAt = LocalDateTime.now();
  }
}

