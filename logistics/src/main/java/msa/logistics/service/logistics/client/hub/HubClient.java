package msa.logistics.service.logistics.client.hub;

import java.util.UUID;
import msa.logistics.service.logistics.client.fallback.HubFallbackFactory;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub", fallbackFactory = HubFallbackFactory.class)
public interface HubClient extends HubService {

    @GetMapping("/api/v1/hub/{hubId}")
    HubResponseDto getHub(@PathVariable("hubId") UUID hubId, @RequestHeader("X-User-Name") String username,
                          @RequestHeader("X-User-Roles") String roles);

    @GetMapping("/api/v1/vendors/{vendorId}")
    VendorResponseDto getVendor(@PathVariable("vendorId") UUID vendorId, @RequestHeader("X-User-Name") String username,
                                @RequestHeader("X-User-Roles") String roles);

}
