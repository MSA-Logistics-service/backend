package msa.logistics.service.logistics.product.service;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.client.hub.HubService;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.VendorService;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final HubService hubService;


    // 상품 생성
    @Transactional
    public UUID createProduct(ProductCreateRequestDto request) {
        // 업체 존재 여부 확인
        VendorResponseDto vendor = Optional.ofNullable(hubService.getVendor(request.getVendorId()))
                .orElseThrow(() -> new CustomException(ErrorCode.VENDOR_NOT_FOUND));

        // 허브 존재 여부 확인
        HubResponseDto hub = Optional.ofNullable(hubService.getHub(request.getHubId()))
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_NOT_FOUND));

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
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(UUID productId) {
        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductResponseDto(product);
    }

    // 특정 업체의 상품 목록 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByVendor(UUID vendorId) {
        // 업체 존재 여부 확인
        VendorResponseDto vendor = Optional.ofNullable(hubService.getVendor(vendorId))
                .orElseThrow(() -> new CustomException(ErrorCode.VENDOR_NOT_FOUND));

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
