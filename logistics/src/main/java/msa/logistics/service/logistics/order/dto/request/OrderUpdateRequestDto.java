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
public class OrderUpdateRequestDto {

    private Long quantity;
    private UUID productId;
    private UUID deliveryId;

}
