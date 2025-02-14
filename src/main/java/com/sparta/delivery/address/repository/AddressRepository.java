package com.sparta.delivery.address.repository;

import com.sparta.delivery.address.entity.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
