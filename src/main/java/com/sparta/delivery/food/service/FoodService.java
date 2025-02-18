package com.sparta.delivery.food.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.food.dto.FoodRequestDto;
import com.sparta.delivery.food.dto.FoodResponseDto;
import com.sparta.delivery.food.entity.Food;
import com.sparta.delivery.food.repository.FoodRepository;
import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.store.entity.Store;
import com.sparta.delivery.store.repository.StoreRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sparta.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final FoodRepository foodRepository;
    private final StoreRepository storeRepository;

    public String addFood(FoodRequestDto requestDto, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "해당 권한을 가지고 있지 않습니다. ");
        }


        Food food = new Food(requestDto);
        UUID storeId = requestDto.getStoreId();

        if (storeId != null) {
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 storeId 입니다. "));

            food.updateStore(store);
        }

        foodRepository.save(food);
        return "등록 완료";
    }


    public FoodResponseDto getFood(UUID foodId) {
        Optional<Food> food = this.foodRepository.findById(foodId);

        if (food.isPresent()) {

            return new FoodResponseDto(food.get());
        } else {

            throw new GlobalException(HttpStatus.NO_CONTENT, "해당하는 음식이 존재하지 않습니다. ");
        }
    }


    public String updateFood(UUID foodId, FoodRequestDto requestDto, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "해당 권한을 가지고 있지 않습니다. ");
        }

        Food food = this.foodRepository.findById(foodId).orElseThrow(() -> new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 음식 id 입니다."));

        food.update(requestDto);
        UUID storeId = requestDto.getStoreId();

        if (storeId != null) {

            Store store = storeRepository.findById(storeId).orElseThrow(() -> new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 storeId 입니다. "));
            food.updateStore(store);
        }

        this.foodRepository.save(food);

        return "수정 완료";
    }

    public String deleteFood(UUID foodId, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "해당 권한을 가지고 있지 않습니다. ");
        }

        Optional<Food> food = this.foodRepository.findById(foodId);

        if (food.isPresent()) {

            foodRepository.delete(food.get());
            return "삭제 완료";
        } else {

            throw new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 음식 입니다.");
        }
    }

    public List<Food> listFood(String keyword) {

        return this.foodRepository.findByName(keyword);
    }

    public String visibleFood(UUID foodId, boolean isVisible, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "해당 권한을 가지고 있지 않습니다. ");
        }

        Food food = this.foodRepository.findById(foodId).orElseThrow(() -> new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 음식 입니다. "));

        food.updateVisible(isVisible);
        this.foodRepository.save(food);

        return isVisible ? "숨김처리 해제 되었습니다. " : "숨김처리 되었습니다. ";
    }
}