package com.sparta.delivery.gemini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.delivery.aaglobal.GlobalException;
import com.sparta.delivery.gemini.dto.GeminiResponseDto;
import com.sparta.delivery.gemini.entity.Gemini;
import com.sparta.delivery.gemini.repository.GeminiRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sparta.delivery.jwt.UserDetailsImpl;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper
    private final WebClient webClient;
    private final GeminiRepository geminiRepository;

    @Value("${gemini.secret.key}")
    private String secretKey;

    public String sendJsonRequest(String question, UserDetailsImpl userDetails) throws JsonProcessingException {

        // CUSTOMER 이면 권한이 옳지 않아 Exception
        roleCheckIfCustomer(userDetails);

        // JSON 데이터 생성
        Map<String, Object> requestBody = createMapObject(question);

        // WebClient 요청하고 데이터 받아오기
        String jsonData = sendGeminiAndGetJsonData(requestBody);

        // 응답에서 대답만 추출
        String answer = extractTextFromJson(jsonData);

        // 해당 내용을 저장
        save(question, answer, userDetails.getUser());

        // 대답만 return
        return answer;
    }

    public String getGemini(UUID aiId, UserDetailsImpl userDetails) {

        // CUSTOMER 이면 Exception
        roleCheckIfCustomer(userDetails);

        Gemini gemini = this.geminiRepository.findById(aiId).orElseThrow(() -> new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 아이디 입니다, "));
        if (gemini.getDeletedAt() != null) {
            throw new GlobalException(HttpStatus.NOT_ACCEPTABLE, "삭제된 메세지 입니다. ");
        }
        return gemini.getAnswer();
    }

    public Page<GeminiResponseDto> getGeminiList(UserDetailsImpl userDetails, int page, int size) {
        // CUSTOMER 이면 Exception
        roleCheckIfCustomer(userDetails);

        Pageable pageable = PageRequest.of(page, size);

        List<GeminiResponseDto> list = this.geminiRepository.findByUserIdOrderByCreatedAtDesc(userDetails.getUser().getId(), pageable)
                .stream().map(GeminiResponseDto::new).toList();
        Page<GeminiResponseDto> dtoPage = new PageImpl<>(list, pageable, list.size());

        return dtoPage;
    }

    public String deleteGemini(UUID aiId, UserDetailsImpl userDetails) {
        roleCheckIfCustomer(userDetails);

        Gemini gemini = this.geminiRepository.findById(aiId).orElseThrow(() -> new GlobalException(HttpStatus.NO_CONTENT, "존재하지 않는 내역입니다. "));
        gemini.updateDelete(userDetails.getUser().getId());
        this.geminiRepository.save(gemini);

        return "해당 내용이 삭제되었습니다. ";
    }

    private Map<String, Object> createMapObject(String question) {
        return Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", question)))));
    }

    private String sendGeminiAndGetJsonData(Map<String, Object> requestBody) {
        return webClient.post().uri(uriBuilder -> uriBuilder.queryParam("key", secretKey).build()) // 실제 요청할 엔드포인트로 변경
                .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody) // JSON 데이터 추가
                .retrieve().bodyToMono(String.class) // 응답을 String으로 받음
                .block(); // 동기 방식으로 실행
    }

    private void save(String question, String answer, User user) throws JsonProcessingException {
        Gemini gemini = new Gemini(jsonToText(question), answer, user);

        this.geminiRepository.save(gemini);
    }

    private String jsonToText(String jsonData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);

        return jsonNode.get("message").asText();
    }

    private String extractTextFromJson(String jsonData) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            return rootNode.path("candidates").get(0)  // 첫 번째 candidate
                    .path("content").path("parts").get(0)  // 첫 번째 part
                    .path("text").asText(); // "text" 값 추출
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response JSON";
        }
    }

    private void roleCheckIfCustomer(UserDetailsImpl userDetails) {
        if (userDetails.getRole().equals(UserRoleEnum.CUSTOMER)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "사용 권한이 없습니다. ");
        }
    }
}
