package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.PaymentStatus;
import com.sparta.delivery.user.entity.User;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PaymentRequestDto {

  private String userNickname;
  private int price;
  private PaymentStatus status;
  private UUID orderId;

  public PaymentRequestDto(User user, )
}
