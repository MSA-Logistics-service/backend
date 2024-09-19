package msa.logistics.service.logistics.client.hub;

import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface HubService {

    HubResponseDto getHub(UUID hubId);

    VendorResponseDto getVendor(UUID vendorId);

}
