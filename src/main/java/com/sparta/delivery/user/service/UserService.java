package com.sparta.delivery.user.service;

import com.sparta.delivery.user.dto.UserRequestDto;
import com.sparta.delivery.user.dto.UserResponseDto;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final String ADMIN_KEY = "deliveryServiceAdminKey";

  @Transactional
  public void signup(UserRequestDto requestDto) {
    duplicationCheck(requestDto.getUsername(), requestDto.getEmail());

    // 사용자 role은 OWNER 또는 CUSTOMER로 설정, MASTER는 관리자 api로 구현
    UserRoleEnum role = requestDto.isOwner() ? UserRoleEnum.OWNER : UserRoleEnum.CUSTOMER;
    String password = passwordEncoder.encode(requestDto.getPassword());
    User user = new User(requestDto, password, role);
    userRepository.save(user);

    user.updateSignupByUserId(user.getId());
    userRepository.save(user);
  }

  public void adminSignup(UserRequestDto requestDto) {
    duplicationCheck(requestDto.getUsername(), requestDto.getEmail());
    checkAdminKey(requestDto.getAdminKey());

    String password = passwordEncoder.encode(requestDto.getPassword());
    User user = new User(requestDto, password, UserRoleEnum.MASTER);
    userRepository.save(user);

    user.updateSignupByUserId(user.getId());
    userRepository.save(user);
  }

  public UserResponseDto getUser(User user) {
    return new UserResponseDto(user);
  }

  public void update(User user, UserRequestDto requestDto) {
    user.update(requestDto);
    userRepository.save(user);
  }

  public void delete(User user) {
    user.updateDelete(user.getId());
    userRepository.save(user);
  }

  private void duplicationCheck(String username, String email) {
    userRepository.findByUsername(username).ifPresent((m) -> {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    });

    userRepository.findByEmail(email).ifPresent((m) -> {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    });
  }

  private void checkAdminKey(String adminKey) {
    if (!ADMIN_KEY.equals(adminKey)) {
      throw new IllegalArgumentException("관리자 키가 일치하지 않습니다.");
    }
  }
}