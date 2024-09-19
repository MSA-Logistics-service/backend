package msa.logistics.service.logistics.product.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.product.service.ProductAiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-content")
public class ProductAiController {

    private final ProductAiService aiService;

    /**
     * 상품 설명 추천 AI 응답 받기
     */
    @GetMapping
    @PreAuthorize("hasAuthority('VENDOR_MANAGER')")
    public ResponseEntity<ApiResponseDto<String>> generateContent(@RequestHeader(value = "X-USER-NAME") String username,
                                                                  @RequestParam(name = "prompt") String prompt) {

        return ResponseEntity.ok().body(new ApiResponseDto<>(HttpStatus.OK, "AI 응답 반환 성공", aiService.createAiResponse(username, prompt)));
    }
}
