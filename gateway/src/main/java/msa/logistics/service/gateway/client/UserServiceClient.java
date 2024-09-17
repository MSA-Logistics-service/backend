package msa.logistics.service.gateway.client;

import msa.logistics.service.gateway.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);
}
