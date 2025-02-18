package com.sparta.delivery.gemini.controller;

import com.sparta.delivery.gemini.service.GeminiService;
import com.sparta.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GeminiController {

  private final GeminiService geminiService;

  @PostMapping()
  public String createGemini(@RequestBody String message, @AuthenticationPrincipal User user) {

    return this.geminiService.sendJsonRequest(message, user);
  }
}
