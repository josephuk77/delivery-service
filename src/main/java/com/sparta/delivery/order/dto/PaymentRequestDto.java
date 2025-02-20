package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.PaymentStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

  private String userNickname;
  private int price;
  private PaymentStatus status;
  private UUID orderId;

}
