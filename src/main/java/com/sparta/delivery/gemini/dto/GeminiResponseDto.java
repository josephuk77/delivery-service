package com.sparta.delivery.gemini.dto;

import com.sparta.delivery.gemini.entity.Gemini;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GeminiResponseDto {

    private UUID geminiId;
    private String question;
    private String answer;

    public GeminiResponseDto(Gemini gemini) {
        this.geminiId = gemini.getId();
        this.question = gemini.getQuestion();
        this.answer = gemini.getAnswer();
    }
}
