package com.sparta.delivery.order.controller;

import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

}
