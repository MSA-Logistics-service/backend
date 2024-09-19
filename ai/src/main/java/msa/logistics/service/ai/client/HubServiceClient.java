package msa.logistics.service.ai.client;

import msa.logistics.service.ai.dto.Hub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "hub")
public interface HubServiceClient {

    @GetMapping("/api/v1/hub")
    Page<Hub> getHubs();


}
