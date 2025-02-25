package com.sparta.delivery.order.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.order.dto.OrderFoodRequestDto;
import com.sparta.delivery.order.service.OrderFoodService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-foods")
@RequiredArgsConstructor
public class OrderFoodController {

  private final OrderFoodService orderFoodService;

  @PostMapping
  public ResponseEntity<?> addOrderFood(@RequestBody OrderFoodRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    orderFoodService.addOrderFood(requestDto, userDetails.getUser());
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{orderFoodId}")
  public ResponseEntity<?> updateOrderFood(@PathVariable UUID orderFoodId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam int quantity) {
    orderFoodService.updateOrderFood(orderFoodId, userDetails.getUser(), quantity);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{orderFoodId}")
  public ResponseEntity<?> deleteOrderFood(@PathVariable UUID orderFoodId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    orderFoodService.deleteOrderFood(orderFoodId, userDetails.getUser());
    return ResponseEntity.ok().build();
  }
}
