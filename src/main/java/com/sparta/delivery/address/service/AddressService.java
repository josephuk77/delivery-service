package com.sparta.delivery.address.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.address.dto.AddressRequestDto;
import com.sparta.delivery.address.dto.AddressResponseDto;
import com.sparta.delivery.address.entity.Address;
import com.sparta.delivery.address.repository.AddressRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import java.util.List;
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
    checkDeletion(address);
    return new AddressResponseDto(address);
  }

  public List<AddressResponseDto> getAllAddress(User user) {
    List<Address> addressList = addressRepository.findAllByUser(user);
    return addressList.stream()
        .map(AddressResponseDto::new)
        .toList();
  }

  @Transactional
  public void updateAddress(UUID addressID, User user, AddressRequestDto requestDto) {
    Address address = findAddress(addressID);
    checkDeletion(address);
    checkPermission(user, address);
    address.update(requestDto);
  }

  @Transactional
  public void deleteAddress(UUID addressID, User user) {
    Address address = findAddress(addressID);
    checkPermission(user, address);
    address.updateDelete(user.getId());
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

  private void checkDeletion(Address address) {
    if (address.getDeletedAt() != null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "삭제된 주소입니다.");
    }
  }
}
