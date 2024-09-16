package msa.logistics.service.logistics.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {
    private Integer quantity;
    private UUID supplierVendorId;
    private UUID receiverVendorId;
    private UUID productId;
    private UUID deliveryId;
}
