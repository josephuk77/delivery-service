package com.sparta.delivery.food.controller;

import com.sparta.delivery.food.dto.FoodRequestDto;
import com.sparta.delivery.food.dto.FoodResponseDto;
import com.sparta.delivery.food.service.FoodService;
import java.util.UUID;

import com.sparta.delivery.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
  public Page<FoodResponseDto> listFood(@PathVariable String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {

    return this.foodService.listFood(keyword, page, size);
  }

  @GetMapping("/store/{storeId}")
  public Page<FoodResponseDto> getFoodsByStoreId(@PathVariable UUID storeId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size){

    return this.foodService.getFoodsByStoreId(storeId, page, size);
  }

  @PatchMapping("/{foodId}")
  public String visibleFood(@PathVariable UUID foodId,
                            @RequestParam(required = false) boolean isVisible,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return this.foodService.visibleFood(foodId, isVisible, userDetails);
  }
}
