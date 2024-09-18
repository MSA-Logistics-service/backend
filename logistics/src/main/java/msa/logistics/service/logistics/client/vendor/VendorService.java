package msa.logistics.service.logistics.client.vendor;

import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface VendorService {
    @GetMapping("/api/v1/vendor/{vendorId}")
    VendorResponseDto getVendor(@PathVariable("vendorId") UUID vendorId);
}
