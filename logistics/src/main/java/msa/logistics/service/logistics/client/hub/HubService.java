package msa.logistics.service.logistics.client.hub;

import java.util.UUID;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;

public interface HubService {

    HubResponseDto getHub(UUID hubId, String username, String roles);

    VendorResponseDto getVendor(UUID vendorId, String username, String roles);

}
