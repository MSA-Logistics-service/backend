package msa.logistics.service.ai.client;

import java.util.List;
import msa.logistics.service.ai.common.dto.ApiResponseDto;
import msa.logistics.service.ai.dto.DeliveryResponseDto;
import msa.logistics.service.ai.dto.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "logistics")
public interface LogisticsServiceClient {

    @GetMapping("/api/v1/orders")
    ApiResponseDto<List<OrderResponseDto>> getAllOrders();

    @GetMapping("/api/v1/deliveries/{deliveryId}")
    ApiResponseDto<DeliveryResponseDto> getDelivery(@PathVariable("deliveryId") Long deliveryId);
}
