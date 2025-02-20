package com.sparta.delivery.user.service;

import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.user.dto.UserRequestDto;
import com.sparta.delivery.user.dto.UserResponseDto;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import com.sparta.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, String> redisTemplate;


  @Value("${admin.key}")
  private String ADMIN_KEY;

  @Transactional
  public void signup(UserRequestDto requestDto) {
    duplicationCheck(requestDto.getUsername(), requestDto.getEmail());

    // 사용자 role은 OWNER 또는 CUSTOMER로 설정, MASTER는 관리자 api로 구현
    UserRoleEnum role = requestDto.isOwner() ? UserRoleEnum.OWNER : UserRoleEnum.CUSTOMER;
    String password = passwordEncoder.encode(requestDto.getPassword());
    User user = new User(requestDto, password, role);
    userRepository.save(user);

    user = findUser(user.getId());
    user.updateSignupByUserId(user.getId());
  }

  @Transactional
  public void adminSignup(UserRequestDto requestDto) {
    duplicationCheck(requestDto.getUsername(), requestDto.getEmail());
    checkAdminKey(requestDto.getAdminKey());

    String password = passwordEncoder.encode(requestDto.getPassword());
    User user = new User(requestDto, password, UserRoleEnum.MASTER);
    userRepository.save(user);

    user = findUser(user.getId());
    user.updateSignupByUserId(user.getId());
  }

  public UserResponseDto getUser(User user) {
    return new UserResponseDto(user);
  }

  public void updateUser(User user, UserRequestDto requestDto) {
    user.update(requestDto);
    userRepository.save(user);
  }

  public void deleteUser(User user) {
    user.updateDelete(user.getId());
    redisTemplate.delete(user.getUsername() );
    userRepository.save(user);
  }

  private void duplicationCheck(String username, String email) {
    userRepository.findByUsername(username).ifPresent((m) -> {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자 이름입니다.");
    });

    userRepository.findByEmail(email).ifPresent((m) -> {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.");
    });
  }

  private void checkAdminKey(String adminKey) {
    if (!adminKey.equals(ADMIN_KEY)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "관리자 키가 일치하지 않습니다.");
    }
  }

  private User findUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
  }

  public void logout(UserDetailsImpl userDetails) {

    String username = userDetails.getUser().getUsername();
    redisTemplate.delete(username);
  }
}