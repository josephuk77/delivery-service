package com.sparta.delivery.address.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.address.dto.AddressRequestDto;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_address")
public class Address extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "address_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "address_name")
  private String name;

  @Column
  private String address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Address(User user, AddressRequestDto requestDto) {
    this.user = user;
    this.name = requestDto.getName();
    this.address = requestDto.getAddress();
  }

  public void update(AddressRequestDto requestDto) {
    this.name = requestDto.getName();
    this.address = requestDto.getAddress();
  }
}
