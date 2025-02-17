package com.sparta.delivery.order.controller;

import com.sparta.delivery.order.dto.PaymentRequestDto;
import com.sparta.delivery.order.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  public RequestEntity<?> createPayment(@RequestBody PaymentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    paymentService.createPayment(requestDto, userDetails.getUser());
    return RequestEntity.ok().build();
  }

}
