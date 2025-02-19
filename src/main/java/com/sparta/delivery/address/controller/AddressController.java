package com.sparta.delivery.address.controller;

import com.sparta.delivery.address.dto.AddressRequestDto;
import com.sparta.delivery.address.dto.AddressResponseDto;
import com.sparta.delivery.address.service.AddressService;
import com.sparta.delivery.jwt.UserDetailsImpl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity createAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody AddressRequestDto requestDto) {
    addressService.createAddress(userDetails.getUser(), requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("주소 등록이 완료되었습니다.");
  }

  @GetMapping("/{addressID}")
  public ResponseEntity<AddressResponseDto> getAddress(
      @PathVariable UUID addressID,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    AddressResponseDto responseDto = addressService.getAddress(addressID, userDetails.getUser());
    return ResponseEntity.ok().body(responseDto);
  }

  @GetMapping()
  public ResponseEntity<List<AddressResponseDto>> getAllAddress(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<AddressResponseDto> responseDto = addressService.getAllAddress(userDetails.getUser());
    return ResponseEntity.ok().body(responseDto);
  }

  @PatchMapping("/{addressID}")
  public ResponseEntity<?> updateAddress(
      @PathVariable UUID addressID,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody AddressRequestDto requestDto) {
    addressService.updateAddress(addressID, userDetails.getUser(), requestDto);
    return ResponseEntity.status(HttpStatus.OK).body("주소 수정이 완료되었습니다.");
  }

  @PatchMapping("/{addressID}/current")
  public ResponseEntity<?> updateCurrentAddress(
      @PathVariable UUID addressID,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    addressService.updateCurrentAddress(addressID, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body("해당 주소가 현재 주소로 설정되었습니다.");
  }

  @DeleteMapping("/{addressID}")
  public ResponseEntity<?> deleteAddress(
      @PathVariable UUID addressID,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    addressService.deleteAddress(addressID, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("주소 삭제가 완료되었습니다.");
  }
}
