package msa.logistics.service.logistics.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.order.domain.Order;
import msa.logistics.service.logistics.product.domain.Product;
import msa.logistics.service.logistics.product.dto.response.ProductResponseDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private UUID orderId;
    private Long quantity;
    private UUID supplierVendorId;
    private UUID receiverVendorId;
    private UUID productId;
    private UUID deliveryId;

    @Builder
    public OrderResponseDto (Order order) {
        this.orderId = order.getOrderId();
        this.quantity = order.getQuantity();
        this.supplierVendorId = order.getSupplierVendorId();
        this.receiverVendorId = order.getReceiverVendorId();
        this.productId = order.getProduct().getProductId();
        this.deliveryId = order.getDelivery().getDeliveryId();
    }

}
