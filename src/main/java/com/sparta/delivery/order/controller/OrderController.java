package com.sparta.delivery.order.controller;

import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.service.OrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/create")
  public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    orderService.createOrder(orderRequest, userDetails.getUser());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<?> getOrder(@PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(orderService.getOrder(orderId, userDetails.getUser()));
  }
}
