package com.sparta.delivery.order.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.order.dto.PaymentDetailResponseDto;
import com.sparta.delivery.order.dto.PaymentRequestDto;
import com.sparta.delivery.order.dto.PaymentResponseDto;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderFood;
import com.sparta.delivery.order.entity.Payment;
import com.sparta.delivery.order.entity.PaymentStatus;
import com.sparta.delivery.order.repository.OrderFoodRepository;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.order.repository.PaymentRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final OrderFoodRepository orderFoodRepository;

  public void createPayment(PaymentRequestDto requestDto, User user) {
    userRepository.findById(user.getId())
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

    Order order = orderRepository.findById(requestDto.getOrderId())
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

    paymentRepository.save(new Payment(requestDto, user, order));
  }

  @Transactional(readOnly = true)
  public List<PaymentResponseDto> getPaymentList(User user) {
    List<Payment> paymentList = paymentRepository.findAllByUsernameAndDeletedAtIsNull(
        user.getUsername());
    List<PaymentResponseDto> responseDtoList = new ArrayList<>();

    for (Payment payment : paymentList) {
      responseDtoList.add(new PaymentResponseDto(payment, user));
    }
    return responseDtoList;
  }

  @Transactional(readOnly = true)
  public PaymentDetailResponseDto getPaymentDetail(UUID paymentId, User user) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다."));

    if (!payment.getOrder().getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.NOT_FOUND, "본인의 결제만 조회할 수 있습니다.");
    }

    List<OrderFood> orderFoodList = orderFoodRepository.findByOrderIdAndDeletedAtIsNull(
        payment.getOrder().getId());

    return new PaymentDetailResponseDto(payment, user, orderFoodList);
  }

  @Transactional
  public void updatePayment(UUID paymentId, User user, int payPrice) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다."));

    if (!payment.getOrder().getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "본인의 결제만 결제할 수 있습니다.");
    }

    if (!(payment.getPrice() == payPrice)) {
      payment.updateStatus(PaymentStatus.REFUND);
      throw new GlobalException(HttpStatus.PAYMENT_REQUIRED, "결제 금액이 일치하지 않습니다.");
    }
    payment.updateStatus(PaymentStatus.DONE);
  }

  @Transactional
  public void deletePayment(UUID paymentId, User user) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다."));

    if (!payment.getOrder().getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "본인의 결제만 삭제할 수 있습니다.");
    }

    payment.updateDelete(user.getId());
  }
}
