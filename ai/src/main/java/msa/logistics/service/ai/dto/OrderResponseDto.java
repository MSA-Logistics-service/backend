package msa.logistics.service.ai.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
