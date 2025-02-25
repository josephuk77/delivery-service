package com.sparta.delivery.address.repository;

import com.sparta.delivery.address.entity.Address;
import com.sparta.delivery.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, UUID> {

  @Query("SELECT a FROM Address a WHERE a.user = :user AND a.deletedAt IS NULL ORDER BY a.createdAt DESC, a.updatedAt DESC")
  List<Address> findAllByUser(User user);
}
