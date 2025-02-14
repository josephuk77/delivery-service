package com.sparta.delivery.gemini.repository;

import com.sparta.delivery.gemini.entity.Gemini;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeminiRepository extends JpaRepository<Gemini, UUID> {

}
