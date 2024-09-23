package msa.logistics.service.logistics.client.hub;

import java.util.List;
import java.util.UUID;

import msa.logistics.service.logistics.client.hub.dto.HubPathResponseDto;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;

public interface HubService {

    HubResponseDto getHub(UUID hubId, String username, String roles);

    VendorResponseDto getVendor(UUID vendorId, String username, String roles);

    List<HubPathResponseDto> getHubPathsByStartAndEnd(String startHub, String endHub, String username, String roles);
}
