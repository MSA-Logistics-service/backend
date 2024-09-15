package msa.logistics.service.gateway.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/username/{username}")
    String getUsernameByUsername(@PathVariable("username") String username);

    @GetMapping("/api/v1/users/role/{username}")
    List<String> getRoleByUsername(@PathVariable("username") String username);
}
