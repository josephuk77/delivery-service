package com.sparta.delivery.food.controller;

import com.sparta.delivery.food.dto.FoodRequestDto;
import com.sparta.delivery.food.dto.FoodResponseDto;
import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.service.FoodService;
import java.util.List;
import java.util.UUID;

import com.sparta.delivery.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
@Slf4j
public class FoodController {

  private final FoodService foodService;

  @PostMapping()
  public String addFood(@RequestBody FoodRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return this.foodService.addFood(requestDto, userDetails);
  }

  @GetMapping(value = "/{foodId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FoodResponseDto> getFood(@PathVariable UUID foodId) {
    log.info("get food with id {}", foodId);
    return ResponseEntity.ok(this.foodService.getFood(foodId));
  }

  @PutMapping("/{foodId}")
  public String updateFood(@PathVariable UUID foodId, @RequestBody FoodRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return this.foodService.updateFood(foodId, requestDto, userDetails);
  }

  @DeleteMapping("/{foodId}")
  public String deleteFood(@PathVariable UUID foodId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return this.foodService.deleteFood(foodId, userDetails);
  }

  @GetMapping("/list/{keyword}")
  public List<Food> listFood(@PathVariable String keyword) {

    return this.foodService.listFood(keyword);
  }

  @PatchMapping("/{foodId}")
  public String visibleFood(@PathVariable UUID foodId,
                            @RequestParam(required = false) boolean isVisible,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
    log.info("visibleFood {}", foodId);
    return this.foodService.visibleFood(foodId, isVisible, userDetails);
  }

}
