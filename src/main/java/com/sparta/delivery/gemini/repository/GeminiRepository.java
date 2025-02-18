package com.sparta.delivery.gemini.repository;

import com.sparta.delivery.gemini.entity.Gemini;

import java.util.List;
import java.util.UUID;

import com.sparta.delivery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeminiRepository extends JpaRepository<Gemini, UUID> {

    List<Gemini> findByUserId(Long Id);
}
