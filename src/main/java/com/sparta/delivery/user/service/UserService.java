package com.sparta.delivery.user.service;

import com.sparta.delivery.user.dto.UserRequestDto;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signup(UserRequestDto requestDto) {
    emailDuplicationCheck(requestDto.getEmail());
    String password = passwordEncoder.encode(requestDto.getPassword());

    // 사용자 role은 OWNER 또는 CUSTOMER로 설정, MASTER는 관리자 api로 구현
    UserRoleEnum role = requestDto.isOwner() ? UserRoleEnum.OWNER : UserRoleEnum.CUSTOMER;

    User user = new User(requestDto, password, role);
    userRepository.save(user);
  }

  private void emailDuplicationCheck(String email) {
    userRepository.findByEmail(email).ifPresent((m) -> {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    });
  }
}
