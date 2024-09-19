package msa.logistics.service.user.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.user.common.dto.ApiResponseDto;
import msa.logistics.service.user.dto.SignUpDto;
import msa.logistics.service.user.dto.UserDto;
import msa.logistics.service.user.dto.UserResponseDto;
import msa.logistics.service.user.dto.UserUpdateRequestDto;
import msa.logistics.service.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("/user/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/{user_id}")
    ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable("user_id") Long userId,
                                                                @RequestHeader("X-User-Name") String username,
                                                                @RequestHeader("X-User-Roles") String roles) {
        UserResponseDto userResponseDto = userService.getUserById(userId, username, roles);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "사용자 조회 성공", userResponseDto));
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping()
    ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> userResponseDtos = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "사용자 전체 조회 성공", userResponseDtos));
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @PatchMapping("/{user_id}")
    ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(@PathVariable("user_id") Long userId,
                                                               @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        UserResponseDto userResponseDto = userService.updateUser(userId, userUpdateRequestDto);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "사용자 수정 성공", userResponseDto));
    }

}
