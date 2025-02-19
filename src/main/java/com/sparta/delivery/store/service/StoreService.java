package com.sparta.delivery.store.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.food.dto.FoodWithStoreResponseDto;
import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.repository.FoodRepository;
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
import com.sparta.delivery.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private final UserRepository userRepository;
  private final FoodRepository foodRepository;
  private final ReviewRepository reviewRepository;

  // 음식점 상세 조회
  @Transactional(readOnly = true)
  public StoreDetailResponseDto getStore(UUID storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 가게입니다."));

    List<Food> foods = foodRepository.findAllByStoreId(storeId);

    List<FoodWithStoreResponseDto> foodDtos =
        foods.stream()
            .map(food -> new FoodWithStoreResponseDto(food.getId(), food.getName(),
                food.getContent(), food.getPrice()))
            .collect(Collectors.toList());

    // 리뷰 수 계산
    Integer reviewCount = reviewRepository.countByStoreId(storeId);
    // 리뷰 평점 계산
    BigDecimal ratingAvg = reviewRepository.calculateAverageRatingByStoreId(storeId);

    return new StoreDetailResponseDto(
        storeId,
        String.valueOf(store.getCategory()),
        store.getName(),
        store.getContent(),
        store.getAddress(),
        store.getPhone(),
        ratingAvg,
        reviewCount,
        foodDtos
    );
  }

  // 음식점 이름 검색
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

  // 음식점 카테고리 검색
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
    // 사용자 존재 여부 확인
    userRepository.findByUsername(user.getUsername())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."));

    // 권한 체크
    if (!(user.getRole().equals(UserRoleEnum.MASTER) || user.getRole()
        .equals(UserRoleEnum.OWNER))) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    // 가게 생성
    Store store = storeRepository.save(new Store(requestDto, user));
    return new StoreResponseDto(store);
  }

  @Transactional
  public StoreResponseDto updateStore(UUID storeId, StoreRequestDto requestDto, User user) {
    // 사용자 존재 여부 확인
    userRepository.findByUsername(user.getUsername())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."));

    // 권한 체크
    if (!(user.getRole().equals(UserRoleEnum.MASTER) || user.getRole()
        .equals(UserRoleEnum.OWNER))) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    // 가게 존재 여부 확인
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 가게입니다."));

    // 가게 업데이트
    store.update(requestDto);
    storeRepository.save(store);

    return new StoreResponseDto(store);
  }

  @Transactional
  public void deleteStore(UUID storeId, User user) {
    // 사용자 존재 여부 확인
    userRepository.findByUsername(user.getUsername())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."));

    // 권한 체크
    if (!(user.getRole().equals(UserRoleEnum.MASTER) || user.getRole()
        .equals(UserRoleEnum.OWNER))) {
      throw new GlobalException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    // 가게 존재 여부 확인
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 가게입니다."));

    // 가게 삭제
    store.updateDelete(user.getId());
    storeRepository.save(store);
  }
}
