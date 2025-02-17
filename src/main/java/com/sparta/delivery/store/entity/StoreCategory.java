package com.sparta.delivery.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;

@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum StoreCategory {
  KOREAN_FOOD("한식"),
  CHINESE_FOOD("중식"),
  STREET_FOOD("분식"),
  CHICKEN("치킨"),
  PIZZA("피자"),
  DESSERT("디저트");

  private final String name;

  StoreCategory(String name) {
    this.name = name;
  }
}
