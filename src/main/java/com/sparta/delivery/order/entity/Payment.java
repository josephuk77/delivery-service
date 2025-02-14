package com.sparta.delivery.order.entity;

import com.sparta.delivery.aaglobal.Timestamped;
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

@Entity
@Getter
@Table(name = "p_payments")
public class Payment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "payment_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "order_user")
  private String username;

  @Column(name = "order_price")
  private String price;

  @Column(name = "payment_status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private Order order;
}
