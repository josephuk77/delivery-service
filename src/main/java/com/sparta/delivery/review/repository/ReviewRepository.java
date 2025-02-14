package com.sparta.delivery.review.repository;

import com.sparta.delivery.review.entity.Review;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

}
