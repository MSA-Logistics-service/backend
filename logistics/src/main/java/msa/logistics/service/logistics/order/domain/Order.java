package msa.logistics.service.logistics.order.domain;

import jakarta.persistence.*;
import lombok.*;
import msa.logistics.service.logistics.common.entity.BaseEntity;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.product.domain.Product;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "supplier_vendor_id", nullable = false) // 공급 업체 ID (벤더 모듈에서 연동)
    private UUID supplierVendorId;

    @Column(name = "receiver_vendor_id", nullable = false) // 수령 업체 ID (벤더 모듈에서 연동)
    private UUID receiverVendorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // 상품 FK
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id") // 배송 FK
    private Delivery delivery;

    @Builder
    public Order(Integer quantity, UUID supplierVendorId, UUID receiverVendorId, Product product, Delivery delivery) {
        this.quantity = quantity;
        this.supplierVendorId = supplierVendorId;
        this.receiverVendorId = receiverVendorId;
        this.product = product;
        this.delivery = delivery;
    }
}
