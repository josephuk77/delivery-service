package com.sparta.delivery.user.dto;

import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserResponseDto {

  private String email;
  private String nickname;
  private String currentAddress;
  private UserRoleEnum role;

  public UserResponseDto(User user) {
    this.email = user.getEmail();
    this.nickname = user.getNickname();
    this.currentAddress = user.getCurrentAddress();
    this.role = user.getRole();
  }
}
