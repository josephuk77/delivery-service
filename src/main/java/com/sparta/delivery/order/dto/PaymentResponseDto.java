package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.Payment;
import com.sparta.delivery.order.entity.PaymentStatus;
import com.sparta.delivery.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

  private String userNickname;
  private String storeName;
  private int price;
  private PaymentStatus status;

  public PaymentResponseDto(Payment payment, User user) {
    this.userNickname = user.getNickname();
    this.storeName = payment.getOrder().getStore().getName();
    this.price = payment.getPrice();
    this.status = payment.getStatus();
  }
}
