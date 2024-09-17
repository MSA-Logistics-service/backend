package msa.logistics.service.logistics.product.domain;

import jakarta.persistence.*;
import lombok.*;
import msa.logistics.service.logistics.common.entity.BaseEntity;
import msa.logistics.service.logistics.common.exception.CustomException;
import msa.logistics.service.logistics.common.exception.ErrorCode;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_products")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "stock_quantity", nullable = false)
    private Long stockQuantity;

    @Column(name = "vendor_id", nullable = false) // 업체 아이디
    private UUID vendorId;

    @Column(name = "hub_id", nullable = false)    // 관리 허브 아이디
    private UUID hubId;

    @Column(name = "description")
    private String description;

    @Builder
    public Product(String productName, Long stockQuantity, UUID vendorId, UUID hubId, String description) {
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.vendorId = vendorId;
        this.hubId = hubId;
        this.description = description;
    }

    public void updateProduct(String productName, Long stockQuantity, String description) {
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public void markAsDeleted() {
        this.isDelete = true;
    }

    public void decreaseStock(Long quantity) {
        if (this.stockQuantity < quantity) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStock(Long quantity) {
        this.stockQuantity += quantity;
    }

}
