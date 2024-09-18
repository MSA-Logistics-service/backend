package msa.logistics.service.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.user.domain.User;
import msa.logistics.service.user.dto.SignUpDto;
import msa.logistics.service.user.dto.UserDto;
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
                .orElseThrow(() -> new IllegalArgumentException("없는 username"));
        return UserDto.convertToUserDto(user);
    }
}
