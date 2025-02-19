package com.sparta.delivery.food.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.food.dto.FoodRequestDto;
import com.sparta.delivery.store.entity.Store;
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
@Table(name = "p_foods")
@Getter
@NoArgsConstructor
public class Food extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "food_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "food_name", nullable = false)
  private String name;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "price", nullable = false)
  private Integer price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @Column(name = "is_visible", nullable = false)
  private boolean isVisible;

  public Food(FoodRequestDto foodRequestDto) {

    this.name = foodRequestDto.getFoodName();
    this.content = foodRequestDto.getContent();
    this.price = foodRequestDto.getPrice();
    this.isVisible = true;

  }

  public void update(FoodRequestDto requestDto) {

    this.name = requestDto.getFoodName();
    this.content = requestDto.getContent();
    this.price = requestDto.getPrice();
  }

  public void updateStore(Store store) {
    this.store = store;
  }

  public void updateVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }
}
