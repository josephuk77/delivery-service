package com.sparta.delivery.store.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.review.repository.ReviewRepository;
import com.sparta.delivery.store.dto.StoreDetailResponseDto;
import com.sparta.delivery.store.dto.StoreRequestDto;
import com.sparta.delivery.store.dto.StoreResponseDto;
import com.sparta.delivery.store.dto.StoreSearchResponseDto;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.entity.StoreCategory;
import com.sparta.delivery.store.repository.StoreRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final ReviewRepository reviewRepository;

  @Transactional(readOnly = true)
  public StoreDetailResponseDto getStore(UUID storeId) {
    Store store = findStore(storeId);

    Integer reviewCount = reviewRepository.countByStoreId(storeId);
    BigDecimal ratingAvg = reviewRepository.calculateAverageRatingByStoreId(storeId);

    return new StoreDetailResponseDto(
        storeId,
        String.valueOf(store.getCategory()),
        store.getName(),
        store.getContent(),
        store.getAddress(),
        store.getPhone(),
        ratingAvg,
        reviewCount
    );
  }

  @Transactional(readOnly = true)
  public Page<StoreSearchResponseDto> getStoresByKeyword(
      String keyword,
      int page,
      int size,
      String sortedBy,
      Sort.Direction direction
  ) {
    Pageable pageable = PageRequest.of(page, size, direction, sortedBy);
    Page<Store> storePage = storeRepository.findAllByName(keyword, pageable);

    return storePage.map(store -> {
      Integer reviewCount = reviewRepository.countByStoreId(store.getId());
      BigDecimal ratingAvg = reviewRepository.calculateAverageRatingByStoreId(store.getId());
      return new StoreSearchResponseDto(store.getId(), store.getName(), ratingAvg, reviewCount);
    });
  }

  @Transactional(readOnly = true)
  public Page<StoreSearchResponseDto> getStoresByCategory(
      String category,
      int page,
      int size,
      String sortedBy,
      Sort.Direction direction
  ) {
    StoreCategory storeCategory = StoreCategory.fromString(category);
    Pageable pageable = PageRequest.of(page, size, direction, sortedBy);
    Page<Store> storePage = storeRepository.findAllByCategory(storeCategory, pageable);

    return storePage.map(store -> {
      Integer reviewCount = reviewRepository.countByStoreId(store.getId());
      BigDecimal ratingAvg = reviewRepository.calculateAverageRatingByStoreId(store.getId());
      return new StoreSearchResponseDto(store.getId(), store.getName(), ratingAvg, reviewCount);
    });
  }

  @Transactional
  public StoreResponseDto createStore(StoreRequestDto requestDto, User user) {
    checkUserRole(user);

    Store store = storeRepository.save(new Store(requestDto, user));
    return new StoreResponseDto(store);
  }

  @Transactional
  public StoreResponseDto updateStore(UUID storeId, StoreRequestDto requestDto, User user) {
    checkUserRole(user);

    Store store = findStore(storeId);

    validateStoreOwner(user, store);

    store.update(requestDto);
    storeRepository.save(store);

    return new StoreResponseDto(store);
  }

  @Transactional
  public void deleteStore(UUID storeId, User user) {
    checkUserRole(user);

    Store store = findStore(storeId);

    validateStoreOwner(user, store);

    store.updateDelete(user.getId());
    storeRepository.save(store);
  }

  private void checkUserRole(User user) {
    if (!(user.getRole().equals(UserRoleEnum.MASTER) || user.getRole()
        .equals(UserRoleEnum.OWNER))) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
  }

  private Store findStore(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 가게입니다."));
  }

  private static void validateStoreOwner(User user, Store store) {
    if (user.getRole().equals(UserRoleEnum.OWNER) && !store.getUser().equals(user)) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }
  }
}
