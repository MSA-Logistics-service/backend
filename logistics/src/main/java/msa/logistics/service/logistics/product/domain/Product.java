package msa.logistics.service.logistics.product.domain;

import jakarta.persistence.*;
import lombok.*;
import msa.logistics.service.logistics.common.entity.BaseEntity;

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

    @Builder
    public Product(String productName, Long stockQuantity, UUID vendorId, UUID hubId) {
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.vendorId = vendorId;
        this.hubId = hubId;
    }

    public void updateProduct(String productName, Long stockQuantity) {
        this.productName = productName;
        this.stockQuantity = stockQuantity;
    }

    public void markAsDeleted() {
        this.isDelete = true;
    }

}
