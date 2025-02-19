package com.sparta.delivery.address.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.address.dto.AddressRequestDto;
import com.sparta.delivery.address.dto.AddressResponseDto;
import com.sparta.delivery.address.entity.Address;
import com.sparta.delivery.address.repository.AddressRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;

  public void createAddress(User user, AddressRequestDto requestDto) {
    Address address = new Address(user, requestDto);
    addressRepository.save(address);
  }

  public AddressResponseDto getAddress(UUID addressID, User user) {
    Address address = findAddress(addressID);
    checkPermission(user, address);

    return new AddressResponseDto(address);
  }

  private Address findAddress(UUID addressID) {
    return addressRepository.findById(addressID).orElseThrow(() ->
        new GlobalException(HttpStatus.NOT_FOUND, "주소를 찾을 수 없습니다."));
  }

  private void checkPermission(User user, Address address) {
    if (user.getRole().equals(UserRoleEnum.MASTER)) {
      return;
    }
    if (!address.getUser().getId().equals(user.getId())) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "해당 주소에 접근할 수 없습니다.");
    }
  }
}
