package msa.logistics.service.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.user.dto.SignUpDto;
import msa.logistics.service.user.dto.UserDto;
import msa.logistics.service.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    Boolean registerUser(@RequestBody SignUpDto signUpDto) {
        return userService.registerUser(signUpDto);
    }

    @GetMapping("/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }
}
