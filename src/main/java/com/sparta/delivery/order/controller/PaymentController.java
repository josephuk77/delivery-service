package com.sparta.delivery.order.controller;

import com.sparta.delivery.order.dto.PaymentRequestDto;
import com.sparta.delivery.order.service.PaymentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    paymentService.createPayment(requestDto, userDetails.getUser());
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<?> getPaymentList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(paymentService.getPaymentList(userDetails.getUser()));
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<?> getPaymentDetail(@PathVariable UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(paymentService.getPaymentDetail(paymentId, userDetails.getUser()));
  }

  @PutMapping("/{paymentId}")
  public ResponseEntity<?> updatePayment(@PathVariable UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam int payPrice) {
    paymentService.updatePayment(paymentId, userDetails.getUser(), payPrice);
    return ResponseEntity.ok().build();
  }
}
