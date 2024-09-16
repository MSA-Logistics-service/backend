package msa.logistics.service.logistics.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.logistics.service.logistics.delivery.domain.Delivery;
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

}
