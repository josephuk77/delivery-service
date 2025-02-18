package com.sparta.delivery.gemini.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.gemini.dto.GeminiResponseDto;
import com.sparta.delivery.gemini.entity.Gemini;
import com.sparta.delivery.gemini.service.GeminiService;
import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class GeminiController {
    private final GeminiService geminiService;

    @PostMapping()
    public String createGemini(@RequestBody String message, @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        if (userDetails.getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "사용 권한이 없습니다. ");
        }

        return this.geminiService.sendJsonRequest(message, userDetails);
    }

    @GetMapping("/{aiId}")
    public String getGemini(@PathVariable UUID aiId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "사용 권한이 없습니다. ");
        }

        return this.geminiService.getGemini(aiId);
    }

    @GetMapping("/list")
    public List<GeminiResponseDto> getGeminiList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "사용 권한이 없습니다. ");
        }

        return this.geminiService.getGeminiList(userDetails.getUser());
    }

    @DeleteMapping("/{aiId}")
    public String deleteGemini(@PathVariable UUID aiId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "사용 권한이 없습니다. ");
        }

        return this.geminiService.deleteGemini(aiId, userDetails.getUser());
    }
}
