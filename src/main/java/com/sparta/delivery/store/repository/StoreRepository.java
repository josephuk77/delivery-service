package com.sparta.delivery.store.repository;

import com.sparta.delivery.store.entity.Store;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

}
