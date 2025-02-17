package com.sparta.delivery.order.service;

import com.sparta.delivery.order.dto.PaymentRequestDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.Payment;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.order.repository.PaymentRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;

  public void createPayment(PaymentRequestDto requestDto, User user) {
    userRepository.findById(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    Order order = orderRepository.findById(requestDto.getOrderId())
        .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    paymentRepository.save(new Payment(requestDto, user, order));
  }


}
