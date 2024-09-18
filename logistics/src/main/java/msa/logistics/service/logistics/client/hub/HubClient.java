package msa.logistics.service.logistics.client.hub;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub")
public interface HubClient extends HubService {
}
