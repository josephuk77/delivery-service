package com.sparta.delivery.order.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.order.dto.PaymentRequestDto;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_payments")
@NoArgsConstructor
public class Payment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "payment_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "order_user")
  private String username;

  @Column(name = "order_price")
  private int price;

  @Column(name = "payment_status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private Order order;

  public Payment(PaymentRequestDto requestDto, User user, Order order) {
    this.username = user.getNickname();
    this.price = requestDto.getPrice();
    this.status = requestDto.getStatus();
    this.order = order;
  }

  public void updateStatus(PaymentStatus status) {
    this.status = status;
  }
}
