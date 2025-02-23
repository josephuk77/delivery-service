package com.sparta.delivery.order.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.order.dto.OrderRequestDto;
import com.sparta.delivery.order.service.OrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    orderService.createOrder(orderRequest, userDetails.getUser());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<?> getOrderDetail(@PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(orderService.getOrderDetail(orderId, userDetails.getUser()));
  }

  @GetMapping
  public ResponseEntity<?> getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(orderService.getOrderList(userDetails.getUser()));
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<?> updateOrderIsDelivery(@PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam boolean isDelivery) {
    orderService.updateOrderStatus(orderId, isDelivery, userDetails.getUser());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    orderService.deleteOrder(orderId, userDetails.getUser());
    return ResponseEntity.ok().build();
  }
}
