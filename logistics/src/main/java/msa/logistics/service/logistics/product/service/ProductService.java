package msa.logistics.service.logistics.product.service;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.product.domain.Product;
import msa.logistics.service.logistics.product.dto.request.ProductCreateRequestDto;
import msa.logistics.service.logistics.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
                .build();

        productRepository.save(product);
        return product.getProductId();
    }
}
