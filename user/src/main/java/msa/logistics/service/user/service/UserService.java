package msa.logistics.service.user.service;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.user.common.exception.CustomException;
import msa.logistics.service.user.common.exception.ErrorCode;
import msa.logistics.service.user.domain.User;
import msa.logistics.service.user.domain.UserRole;
import msa.logistics.service.user.dto.SignUpDto;
import msa.logistics.service.user.dto.UserDto;
import msa.logistics.service.user.dto.UserResponseDto;
import msa.logistics.service.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public Boolean registerUser(SignUpDto signUpDto) {
        User user = User.convertSignUpDtoToUser(signUpDto);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("회원 가입에서 User 저장 시 오류 발생");
            return false;
        }
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserDto.convertToUserDto(user);
    }

    public UserResponseDto getUserById(Long userId, String username, String roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!isMaster(roles)) {
            if (!user.getUsername().equals(username)) {
                throw new CustomException(ErrorCode.USER_NOT_AUTHORIZATION);
            }
        }

        return UserResponseDto.convertToUserResponseDto(user);
    }

    public Boolean isMaster(String roles) {
        List<UserRole> userRoles = Arrays.stream(roles.split(","))
                .map(role -> UserRole.fromString(role.trim()))
                .toList();

        for (UserRole userRole : userRoles) {
            if (UserRole.MASTER.equals(userRole)) {
                return true;
            }
        }
        return false;
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::convertToUserResponseDto)
                .toList();
    }
}
