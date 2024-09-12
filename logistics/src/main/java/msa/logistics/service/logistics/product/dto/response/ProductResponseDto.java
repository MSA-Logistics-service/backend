package msa.logistics.service.logistics.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.logistics.service.logistics.product.domain.Product;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private UUID productId;
    private String productName;
    private Long stockQuantity;
    private UUID vendorId;
    private UUID hubId;

    @Builder
    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.stockQuantity = product.getStockQuantity();
        this.vendorId = product.getVendorId();
        this.hubId = product.getHubId();
    }
}
