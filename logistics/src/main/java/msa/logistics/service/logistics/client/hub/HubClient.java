package msa.logistics.service.logistics.client.hub;

import java.util.List;
import java.util.UUID;
//import msa.logistics.service.logistics.client.fallback.HubFallbackFactory;
import msa.logistics.service.logistics.client.hub.dto.HubPathResponseDto;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "hub")
//@FeignClient(name = "hub", fallbackFactory = HubFallbackFactory.class)
public interface HubClient extends HubService {
    //허브 조회
    @GetMapping("/api/v1/hub/{hubId}")
    HubResponseDto getHub(@PathVariable("hubId") UUID hubId, @RequestHeader("X-User-Name") String username,
                          @RequestHeader("X-User-Roles") String roles);

    //vendor 조회
    @GetMapping("/api/v1/vendors/{vendorId}")
    VendorResponseDto getVendor(@PathVariable("vendorId") UUID vendorId,
                                @RequestHeader("X-User-Name") String username,
                                @RequestHeader("X-User-Roles") String roles);


    //HubPath 조회
    @GetMapping("/api/v1/hub-path/list")
    List<HubPathResponseDto> getHubPathsByStartAndEnd(
            @RequestParam(value = "startHub", required = false) String startHub,
            @RequestParam(value = "endHub", required = false) String endHub,
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Roles") String roles);




}
