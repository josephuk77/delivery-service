package com.sparta.delivery.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {

  @NotBlank
  private String category;

  @NotBlank
  private String name;

  @NotBlank
  @Size(max = 100)
  private String content;

  @NotBlank
  private String address;

  @NotBlank
  @Pattern(regexp = "^01(0)([0-9]{4})([0-9]{4})$")
  private String phone;
}
