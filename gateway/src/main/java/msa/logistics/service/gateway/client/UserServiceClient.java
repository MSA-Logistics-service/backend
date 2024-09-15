package msa.logistics.service.gateway.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user")
@RequestMapping("/api/v1/users")
public interface UserServiceClient {
    @GetMapping("/username/{username}")
    String getUsernameByUsername(@PathVariable("username") String username);

    @GetMapping("/role/{username}")
    List<String> getRoleByUsername(@PathVariable("username") String username);
}
