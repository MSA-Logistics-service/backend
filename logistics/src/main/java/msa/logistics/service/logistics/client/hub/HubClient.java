package msa.logistics.service.logistics.client.hub;

import msa.logistics.service.logistics.client.vendor.VendorService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub")
public interface HubClient extends HubService {
}
