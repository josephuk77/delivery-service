package com.sparta.delivery.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

  private String nickname;
  private String email;
  private String password;
  private boolean isOwner;
}
