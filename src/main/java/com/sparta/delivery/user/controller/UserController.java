package com.sparta.delivery.user.controller;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.user.dto.UserRequestDto;
import com.sparta.delivery.user.dto.UserResponseDto;
import com.sparta.delivery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody UserRequestDto requestDto) {
    userService.signup(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
  }

  @PostMapping("/admin/signup")
  public ResponseEntity<?> adminSignup(@Valid @RequestBody UserRequestDto requestDto) {
    userService.adminSignup(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("관리자 회원가입이 완료되었습니다.");
  }

  @GetMapping
  public ResponseEntity<UserResponseDto> getUser(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    UserResponseDto responseDto = userService.getUser(userDetails.getUser());
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping
  public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody UserRequestDto requestDto) {
    userService.updateUser(userDetails.getUser(), requestDto);
    return ResponseEntity.ok("회원정보 수정이 완료되었습니다.");
  }

  @DeleteMapping
  public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    userService.deleteUser(userDetails.getUser());
    return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
  }
}
