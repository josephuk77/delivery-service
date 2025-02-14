package com.sparta.delivery.user.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.user.dto.UserRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_users")
public class User extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column
  private String nickname;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;

  @Column
  private String currentAddress;

  public User(UserRequestDto requestDto, String password, UserRoleEnum role) {
    this.nickname = requestDto.getNickname();
    this.email = requestDto.getEmail();
    this.password = password;
    this.role = role;
  }
}
