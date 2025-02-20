package com.sparta.delivery.order.controller;

import com.sparta.delivery.order.dto.OrderFoodRequestDto;
import com.sparta.delivery.order.service.OrderFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-foods")
@RequiredArgsConstructor
public class OrderFoodController {

  private final OrderFoodService orderFoodService;

  @PostMapping
  public ResponseEntity<?> addOrderFood(OrderFoodRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    orderFoodService.addOrderFood(requestDto, userDetails.getUser());
    return ResponseEntity.ok().build();
  }

}
