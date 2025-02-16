package com.sparta.delivery.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserRequestDto {

  @Email
  private String email;
  private String nickname;
  private String password;
  private boolean owner;
  private String currentAddress;
  private String adminKey;
}
