package com.sparta.delivery.store.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.store.dto.StoreRequestDto;
import com.sparta.delivery.store.dto.StoreResponseDto;
import com.sparta.delivery.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @PostMapping
  public ResponseEntity<StoreResponseDto> createStore(
      @Valid @RequestBody StoreRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    StoreResponseDto responseDto = storeService.createStore(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @PutMapping("/{store_id}")
  public ResponseEntity<StoreResponseDto> updateStore(
      @PathVariable UUID store_id,
      @Valid @RequestBody StoreRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    StoreResponseDto responseDto = storeService.updateStore(store_id, requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}
