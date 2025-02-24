package com.sparta.delivery.order.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.order.dto.PaymentRequestDto;
import com.sparta.delivery.order.entity.PaymentStatus;
import com.sparta.delivery.order.service.PaymentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
  public ResponseEntity<?> getPaymentList(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "isDelivery", required = false) PaymentStatus paymentStatus,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortedBy", defaultValue = "createdAt") String sortedBy,
      @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction) {
    return ResponseEntity.ok(paymentService.getPaymentList(userDetails.getUser(), paymentStatus,
        page, size, sortedBy, direction));
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

  @DeleteMapping("/{paymentId}")
  public ResponseEntity<?> deletePayment(@PathVariable UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    paymentService.deletePayment(paymentId, userDetails.getUser());
    return ResponseEntity.ok().build();
  }
}
