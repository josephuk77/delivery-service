package com.sparta.delivery.store.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.store.dto.StoreRequestDto;
import com.sparta.delivery.store.dto.StoreResponseDto;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.repository.StoreRepository;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final UserRepository userRepository;

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

}
