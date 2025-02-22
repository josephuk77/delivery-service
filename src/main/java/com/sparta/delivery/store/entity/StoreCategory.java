package com.sparta.delivery.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.sparta.delivery.aaglobal.GlobalException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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

  public static StoreCategory fromString(String category) {
    for (StoreCategory storeCategory : values()) {
      if (storeCategory.getName().equals(category)) {
        return storeCategory;
      }
    }
    throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다.");
  }
}
