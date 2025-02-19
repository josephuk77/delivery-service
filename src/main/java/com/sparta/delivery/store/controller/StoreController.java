package com.sparta.delivery.store.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.store.dto.StoreDetailResponseDto;
import com.sparta.delivery.store.dto.StoreRequestDto;
import com.sparta.delivery.store.dto.StoreResponseDto;
import com.sparta.delivery.store.dto.StoreSearchResponseDto;
import com.sparta.delivery.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @GetMapping("/{storeId}")
  public ResponseEntity<StoreDetailResponseDto> getStore(@PathVariable UUID storeId) {
    StoreDetailResponseDto responseDto = storeService.getStore(storeId);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @GetMapping("/{keyword}")
  public ResponseEntity<Page<StoreSearchResponseDto>> getStoresByKeyword(
      @PathVariable String keyword,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortedBy", defaultValue = "createdAt") String sortedBy,
      @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction
  ) {
    Page<StoreSearchResponseDto> stores = storeService.getStoresByKeyword(
        keyword, page, size, sortedBy, direction);
    return ResponseEntity.status(HttpStatus.OK).body(stores);
  }

  @GetMapping("/categories")
  public ResponseEntity<Page<StoreSearchResponseDto>> getStoresByCategory(
      @RequestParam String category,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortedBy", defaultValue = "createdAt") String sortedBy,
      @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction) {
    Page<StoreSearchResponseDto> stores = storeService.getStoresByCategory(
        category, page, size, sortedBy, direction);
    return ResponseEntity.status(HttpStatus.OK).body(stores);
  }

  @PostMapping
  public ResponseEntity<StoreResponseDto> createStore(
      @Valid @RequestBody StoreRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    StoreResponseDto responseDto = storeService.createStore(requestDto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @PatchMapping("/{storeId}")
  public ResponseEntity<StoreResponseDto> updateStore(
      @PathVariable UUID storeId,
      @Valid @RequestBody StoreRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    StoreResponseDto responseDto = storeService.updateStore(storeId, requestDto,
        userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @DeleteMapping("/{storeId}")
  public ResponseEntity<?> deleteStore(
      @PathVariable UUID storeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    storeService.deleteStore(storeId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body("삭제가 완료되었습니다.");
  }
}
