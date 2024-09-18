package msa.logistics.service.logistics.client.hub;

import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface HubService {

    @GetMapping("/api/v1/hub/{hubId}")
    HubResponseDto getHub(@PathVariable("hubId") UUID hubId);

    @GetMapping("/api/v1/vendor/{vendorId}")
    VendorResponseDto getVendor(@PathVariable("vendorId") UUID vendorId);

}
