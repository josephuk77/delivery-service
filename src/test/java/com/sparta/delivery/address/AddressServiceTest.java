package com.sparta.delivery.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.address.entity.Address;
import com.sparta.delivery.address.repository.AddressRepository;
import com.sparta.delivery.address.service.AddressService;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
class AddressServiceTest {

  @Autowired
  private AddressService addressService;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  UserRepository userRepository;

  User user;
  Address address;

  @BeforeEach
  void setUp() {
    user = createTestUser();
    address = createTestAddress(user);

    userRepository.save(user);
    addressRepository.save(address);
  }

  @AfterEach
  void tearDown() {
    addressRepository.deleteAll();
    userRepository.deleteAll();
  }

  private User createTestUser() {
    return User.builder()
        .username("test user")
        .email("test@email.com")
        .password("password")
        .role(UserRoleEnum.CUSTOMER)
        .build();
  }

  private Address createTestAddress(User user) {
    return Address.builder()
        .name("집")
        .address("서울시 강남구")
        .user(user)
        .build();
  }

  @Nested
  @DisplayName("유저의 현재 배송지 업데이트 테스트")
  class updateCurrentAddressTest {

    @Test
    @Transactional
    @DisplayName("현재 배송지 업데이트 성공")
    void updateCurrentAddressTest_success() {
      // when
      addressService.updateCurrentAddress(address.getId(), user);

      // then
      assertEquals(user.getCurrentAddress(), address.getAddress());
    }

    @Test
    @DisplayName("현재 배송지 업데이트 실패 - 권한 없음")
    void updateCurrentAddressTest_fail_noPermission() {
      // given
      ReflectionTestUtils.setField(user, "id", 200L);

      // when
      GlobalException e = assertThrows(GlobalException.class, () -> {
        addressService.updateCurrentAddress(address.getId(), user);
      });

      // then
      assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
      assertEquals("해당 주소에 접근할 수 없습니다.", e.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("현재 배송지 업데이트 실패 - 이미 삭제된 주소")
    void updateCurrentAddressTest_fail_deletedAddress() {
      // given
      ReflectionTestUtils.setField(address, "deletedAt", LocalDateTime.now());

      // when
      GlobalException e = assertThrows(GlobalException.class, () -> {
        addressService.updateCurrentAddress(address.getId(), user);
      });

      // then
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
      assertEquals("이미 삭제된 주소입니다.", e.getMessage());
    }

    @Test
    @DisplayName("현재 배송지 업데이트 실패 - 찾을 수 없는 주소")
    void updateCurrentAddressTest_fail_notFoundAddress() {
      // given
      UUID addressId = UUID.fromString("00000000-0000-0000-0000-000000000000");

      // when
      GlobalException e = assertThrows(GlobalException.class, () -> {
        addressService.updateCurrentAddress(addressId, user);
      });

      // then
      assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
      assertEquals("주소를 찾을 수 없습니다.", e.getMessage());
    }
  }

  @Nested
  @DisplayName("배송지 삭제 테스트")
  class deleteAddressTest {

    @Test
    @Transactional
    @DisplayName("배송지 삭제 성공")
    void deleteAddressTest_success() {
      // when
      addressService.deleteAddress(address.getId(), user);

      // then
      assertNotNull(address.getDeletedAt());
      assertEquals(address.getDeletedBy(), user.getId());
    }

    @Test
    @DisplayName("배송지 삭제 실패 - 권한 없음")
    void deleteAddressTest_fail_noPermission() {
      // given
      ReflectionTestUtils.setField(user, "id", 200L);

      // when
      GlobalException e = assertThrows(GlobalException.class, () -> {
        addressService.deleteAddress(address.getId(), user);
      });

      // then
      assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
      assertEquals("해당 주소에 접근할 수 없습니다.", e.getMessage());
    }
  }
}