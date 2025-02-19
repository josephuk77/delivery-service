package com.sparta.delivery.address.service;

import com.sparta.delivery.address.dto.AddressRequestDto;
import com.sparta.delivery.address.entity.Address;
import com.sparta.delivery.address.repository.AddressRepository;
import com.sparta.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;

  public void createAddress(User user, AddressRequestDto requestDto) {
    Address address = new Address(user, requestDto);
    addressRepository.save(address);
  }
}
