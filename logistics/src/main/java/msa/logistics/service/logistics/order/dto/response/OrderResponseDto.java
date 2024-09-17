package msa.logistics.service.logistics.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.order.domain.Order;
import msa.logistics.service.logistics.product.domain.Product;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private UUID orderId;
    private Integer quantity;
    private UUID supplierVendorId;
    private UUID receiverVendorId;
    private Product product;
    private Delivery delivery;

    // Order 엔티티 -> OrderResponseDto
    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .quantity(order.getQuantity())
                .supplierVendorId(order.getSupplierVendorId())
                .receiverVendorId(order.getReceiverVendorId())
                .product(order.getProduct())
                .delivery(order.getDelivery())
                .build();
    }

}
