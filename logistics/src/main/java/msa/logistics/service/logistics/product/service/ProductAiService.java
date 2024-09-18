package msa.logistics.service.logistics.product.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.logistics.product.domain.ProductAiChat;
import msa.logistics.service.logistics.product.repository.ProductAiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
public class ProductAiService {

    private final RestTemplate restTemplate;
    private final ProductAiRepository aiRepository;

    @Value("${ai.google.api-key}")
    private String apiKey;

    @Autowired
    public ProductAiService(RestTemplateBuilder builder, ProductAiRepository aiRepository) {
        this.restTemplate = builder.build();
        this.aiRepository = aiRepository;
    }

    @Transactional
    public String createAiResponse(String prompt) {

        UUID ai_id = UUID.randomUUID();

        String response = extractTextFromResponse(generateContent(prompt));

        ProductAiChat ai = ProductAiChat.builder()
                .productAiChatId(ai_id)
                .response(response)
                .build();
        aiRepository.save(ai);

        return response;
    }

    public String generateContent(String prompt) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://generativelanguage.googleapis.com")
                .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
                .queryParam("key", apiKey)
                .build()
                .toUri();

        log.info("uri = {}", uri);

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 바디 생성
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", prompt + "답변을 최대한 간결하게 50자 이하로");

        List<Map<String, Object>> partsList = new ArrayList<>();
        partsList.add(parts);

        Map<String, Object> contents = new HashMap<>();
        contents.put("parts", partsList);

        List<Map<String, Object>> contentsList = new ArrayList<>();
        contentsList.add(contents);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", contentsList);

        // HTTP 요청 엔티티 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내고 응답 받아오기
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                String.class
        );


        log.info("statusCode = {}", responseEntity.getStatusCode());

        // 응답 바디 반환
        return responseEntity.getBody();
    }

    // JSON 응답에서 text 값 추출 메서드
    private String extractTextFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text");
            return textNode.asText();
        } catch (Exception e) {
            log.error("Error parsing response JSON", e);
            return null;
        }
    }

}
