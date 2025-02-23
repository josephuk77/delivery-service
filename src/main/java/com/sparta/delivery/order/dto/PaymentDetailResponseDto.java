package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.OrderFood;
import com.sparta.delivery.order.entity.Payment;
import com.sparta.delivery.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailResponseDto {

  private String userNickname;
  private String storeName;
  private int price;
  private String status;
  private List<OrderFoodResponseDto> orderFoodList;

  public PaymentDetailResponseDto(Payment payment, User user, List<OrderFood> orderFoodList) {
    this.userNickname = user.getNickname();
    this.storeName = payment.getOrder().getStore().getName();
    this.price = payment.getPrice();
    this.status = payment.getStatus().toString();
    for (OrderFood orderFood : orderFoodList) {
      this.orderFoodList.add(new OrderFoodResponseDto(orderFood));
    }
  }
}
