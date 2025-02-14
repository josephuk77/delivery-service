package com.sparta.delivery.food.controller;

import com.sparta.delivery.food.dto.FoodRequestDto;
import com.sparta.delivery.food.dto.FoodResponseDto;
import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.service.FoodService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {

  private final FoodService foodService;

  @PostMapping()
  public String addFood(@RequestBody FoodRequestDto requestDto) {

    return this.foodService.addFood(requestDto);
  }

  @GetMapping(value = "/{foodId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FoodResponseDto> getFood(@PathVariable UUID foodId) {

    return ResponseEntity.ok(this.foodService.getFood(foodId));
  }

  @PutMapping("/{foodId}")
  public String updateFood(@PathVariable UUID foodId, @RequestBody FoodRequestDto requestDto) {

    return this.foodService.updateFood(foodId, requestDto);
  }

  @DeleteMapping("/{foodId}")
  public String deleteFood(@PathVariable UUID foodId) {

    return this.foodService.deleteFood(foodId);
  }

  @GetMapping("/list/{keyword}")
  public List<Food> listFood(@PathVariable String keyword) {

    return this.foodService.listFood(keyword);
  }
}
