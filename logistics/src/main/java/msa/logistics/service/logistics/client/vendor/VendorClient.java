package msa.logistics.service.logistics.client.vendor;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub")
public interface VendorClient extends VendorService{
}
