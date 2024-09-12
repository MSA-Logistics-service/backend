package msa.logistics.service.logistics.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.product.dto.request.ProductCreateRequestDto;
import msa.logistics.service.logistics.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<UUID>> createProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        UUID productId = productService.createProduct(request);
        return ResponseEntity
                .created(URI.create("/api/v1/products/" + productId))
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "상품이 생성되었습니다.", productId));
    }

}
