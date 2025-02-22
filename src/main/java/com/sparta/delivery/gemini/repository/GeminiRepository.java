package com.sparta.delivery.gemini.repository;

import com.sparta.delivery.gemini.entity.Gemini;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeminiRepository extends JpaRepository<Gemini, UUID> {

    Page<Gemini> findByUserIdOrderByCreatedAtDesc(Long Id, Pageable pageable);
}
