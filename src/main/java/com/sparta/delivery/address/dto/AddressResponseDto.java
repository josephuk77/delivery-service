package com.sparta.delivery.address.dto;

import com.sparta.delivery.address.entity.Address;
import lombok.Getter;

@Getter
public class AddressResponseDto {

  private String name;
  private String address;

  public AddressResponseDto(Address address) {
    this.name = address.getName();
    this.address = address.getAddress();
  }
}
