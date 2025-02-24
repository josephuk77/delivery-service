package com.sparta.delivery.order.repository;

import com.sparta.delivery.order.entity.Payment;
import com.sparta.delivery.order.entity.PaymentStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {


  List<Payment> findAllByUsernameAndDeletedAtIsNull(String username, Pageable pageable);

  List<Payment> findAllByUsernameAndStatusAndDeletedAtIsNull(String username,
      PaymentStatus paymentStatus, Pageable pageable);
}
