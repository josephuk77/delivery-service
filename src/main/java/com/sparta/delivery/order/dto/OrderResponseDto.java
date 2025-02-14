package com.sparta.delivery.order.dto;

import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.user.entity.User;
import java.util.UUID;

public class OrderResponseDto {

  private UUID id;
  private String customerNickname;
  private String request;
  private String address;
  private int totalPrice;
  private boolean isDelivery;
  private String status;
  private String storeName;
  private String storePhone;

  public OrderResponseDto(Order order, Store store, User user) {
    this.id = order.getId();
    this.customerNickname = user.getNickname();
    this.request = order.getRequest();
    this.address = order.getAddress();
    this.totalPrice = order.getTotalPrice();
    this.isDelivery = order.isDelivery();
    this.status = order.getStatus().name();
    this.storeName = store.getName();
    this.storePhone = store.getPhone();
  }
}
