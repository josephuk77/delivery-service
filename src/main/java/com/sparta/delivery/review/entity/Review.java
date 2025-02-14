package com.sparta.delivery.review.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "_p_reviews")
public class Review extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "review_id", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private Integer star;

  @Column(name = "review_content", nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;
}
