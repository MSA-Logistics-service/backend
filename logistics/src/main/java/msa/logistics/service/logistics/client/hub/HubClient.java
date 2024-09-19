package msa.logistics.service.logistics.client.hub;

import msa.logistics.service.logistics.client.fallback.HubFallback;
import msa.logistics.service.logistics.client.fallback.HubFallbackFactory;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub", fallbackFactory = HubFallbackFactory.class)
public interface HubClient extends HubService {

    @GetMapping("/api/v1/hub/{hubId}")
    HubResponseDto getHub(@PathVariable("hubId") UUID hubId);

    @GetMapping("/api/v1/vendors/{vendorId}")
    VendorResponseDto getVendor(@PathVariable("vendorId") UUID vendorId);

}
