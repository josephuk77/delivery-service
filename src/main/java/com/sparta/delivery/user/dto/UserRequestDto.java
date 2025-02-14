package com.sparta.delivery.user.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {

  private String nickname;
  private String email;
  private String password;
  private boolean isOwner;
}
