package com.sparta.delivery.order.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.food.entity.Food;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_foods")
@Getter
@NoArgsConstructor
public class OrderFood extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "order_food_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "food_name", nullable = false)
  private String foodName;

  @Column(name = "food_price", nullable = false)
  private int foodPrice;

  @Column(name = "food_name", nullable = false)
  private int quantity;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "food_id")
  private Food food;
}
