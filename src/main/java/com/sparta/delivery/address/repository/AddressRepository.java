package com.sparta.delivery.address.repository;

import com.sparta.delivery.address.entity.Address;
import com.sparta.delivery.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {

  List<Address> findAllByUser(User user);
}
