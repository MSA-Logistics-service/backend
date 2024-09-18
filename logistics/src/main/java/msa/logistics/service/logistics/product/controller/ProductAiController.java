package msa.logistics.service.logistics.product.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.product.service.ProductAiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-content")
public class ProductAiController {

    private final ProductAiService aiService;

    /**
     * 상품 설명 추천 AI 응답 받기
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<String>> generateContent(@RequestParam(name = "prompt") String prompt) {

        return ResponseEntity.ok().body(new ApiResponseDto<>(HttpStatus.OK, "AI 응답 반환 성공", aiService.createAiResponse(prompt)));
    }
}
