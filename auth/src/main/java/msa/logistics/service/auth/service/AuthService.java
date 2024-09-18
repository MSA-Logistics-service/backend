package msa.logistics.service.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import msa.logistics.service.auth.client.UserServiceClient;
import msa.logistics.service.auth.domain.User;
import msa.logistics.service.auth.dto.LoginRequestDto;
import msa.logistics.service.auth.dto.SignUpDto;
import msa.logistics.service.auth.dto.UserDto;
import msa.logistics.service.auth.security.UserDetailsImpl;
import msa.logistics.service.auth.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final RedisService redisService;

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        Collection<GrantedAuthority> roles = ((UserDetailsImpl) authentication.getPrincipal()).getAuthorities();
        List<String> rolesList = roles.stream().map(GrantedAuthority::getAuthority).toList();
        String rolesString = rolesList.toString();

        User user = (User) authentication.getPrincipal();
        UserDto userDto = UserDto.convertToUserDto(user);
        redisService.setValue("user:" + username, userDto);

        String jwt = jwtUtil.createToken(username, roles);
        jwtUtil.addJwtToHeader(jwt, response);

        response.addHeader("X-User-Id", username);
        response.addHeader("X-User-Role", rolesString);
    }

    public Boolean signup(SignUpDto signUpDto) {
        String password = passwordEncoder.encode(signUpDto.getPassword());
        signUpDto.setPassword(password);
        return userServiceClient.registerUser(signUpDto);
    }
}
