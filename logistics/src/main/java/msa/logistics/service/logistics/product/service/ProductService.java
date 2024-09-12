package msa.logistics.service.logistics.product.service;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.exception.CustomException;
import msa.logistics.service.logistics.common.exception.ErrorCode;
import msa.logistics.service.logistics.product.domain.Product;
import msa.logistics.service.logistics.product.dto.request.ProductCreateRequestDto;
import msa.logistics.service.logistics.product.dto.request.ProductUpdateRequestDto;
import msa.logistics.service.logistics.product.dto.response.ProductResponseDto;
import msa.logistics.service.logistics.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 생성
    @Transactional
    public UUID createProduct(ProductCreateRequestDto request) {
        //TODO: 리팩토링 업체와 허브의 존재 여부 확인 로직 생략
        Product product = Product.builder()
                .productName(request.getProductName())
                .stockQuantity(request.getStockQuantity())
                .vendorId(request.getVendorId())
                .hubId(request.getHubId())
                .description(request.getDescription())
                .build();

        productRepository.save(product);
        return product.getProductId();
    }

    // 상품 상세 조회
    public ProductResponseDto getProduct(UUID productId) {
        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductResponseDto(product);
    }

    // 특정 업체의 상품 목록 조회
    public List<ProductResponseDto> getProductsByVendor(UUID vendorId) {
        List<Product> products = productRepository.findByVendorIdAndIsDeleteFalse(vendorId);
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상품 수정
    @Transactional
    public void updateProduct(UUID productId, ProductUpdateRequestDto request) {
        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateProduct(request.getProductName(), request.getStockQuantity(), request.getDescription());
    }

    // 상품 삭제 (논리적 삭제)
    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.markAsDeleted();
    }
}
