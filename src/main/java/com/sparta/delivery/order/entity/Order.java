package com.sparta.delivery.order.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.store.entity.Store;
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
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_orders")
@Getter
@NoArgsConstructor
public class Order extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "order_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "order_request")
  private String request;

  @Column(name = "order_address", nullable = false)
  private String address;

  @Column(name = "total_price", nullable = false)
  private int totalPrice;

  @Column(name = "is_delivery", nullable = false)
  private boolean isDelivery = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "order_status", nullable = false)
  private OrderStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "store_id")
  private Store store;

  public Order(OrderRequestDto requestDto, User user, Store store, Food food, int quantity) {
    this.request = requestDto.getRequest();
    this.address = requestDto.getAddress();
    this.totalPrice = food.getPrice() * quantity;
    this.isDelivery = false;
    this.status = requestDto.getStatus();
    this.user = user;
    this.store = store;
  }

  public void updateIsDelivery(boolean isDelivery) {
    this.isDelivery = isDelivery;
  }
}
