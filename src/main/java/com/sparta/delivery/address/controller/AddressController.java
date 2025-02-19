package com.sparta.delivery.address.controller;

import com.sparta.delivery.address.dto.AddressRequestDto;
import com.sparta.delivery.address.service.AddressService;
import com.sparta.delivery.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @PostMapping
  public void createAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody AddressRequestDto requestDto) {
    addressService.createAddress(userDetails.getUser(), requestDto);
  }
}
