package msa.logistics.service.logistics.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.product.dto.request.ProductCreateRequestDto;
import msa.logistics.service.logistics.product.dto.request.ProductUpdateRequestDto;
import msa.logistics.service.logistics.product.dto.response.ProductResponseDto;
import msa.logistics.service.logistics.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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
    @PreAuthorize("hasAuthority('VENDOR_MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<ApiResponseDto<UUID>> createProduct(@RequestHeader(value = "X-USER-NAME") String username,
                                                              @Valid @RequestBody ProductCreateRequestDto request) {
        UUID productId = productService.createProduct(username, request);
        return ResponseEntity
                .created(URI.create("/api/v1/products/" + productId))
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "상품 생성 성공", productId));
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getProduct(@PathVariable UUID productId) {
        ProductResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "상품 상세 정보 조회 성공", product));
    }

    /**
     * 특정 업체의 상품 목록 조회
     */
    @GetMapping("/vendors/{vendorId}")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsByVendor(@PathVariable UUID vendorId) {
        List<ProductResponseDto> products = productService.getProductsByVendor(vendorId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "업체의 전체 상품 목록", products));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('VENDOR_MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<ApiResponseDto<Void>> updateProduct(@PathVariable UUID productId,
                                                              @Valid @RequestBody ProductUpdateRequestDto request) {
        productService.updateProduct(productId, request);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "상품 수정 성공", null));
    }

    /**
     * 상품 삭제 (논리적 삭제)
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "상품 삭제 성공", null));
    }

}
