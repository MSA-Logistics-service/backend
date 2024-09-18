package msa.logistics.service.auth.client;

import msa.logistics.service.auth.dto.SignUpDto;
import msa.logistics.service.auth.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);

    @PostMapping("/api/v1/users/sign-up")
    Boolean registerUser(@RequestBody SignUpDto signUpDto);
}
