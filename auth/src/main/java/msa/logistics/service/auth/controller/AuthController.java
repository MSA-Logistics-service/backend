package msa.logistics.service.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import msa.logistics.service.auth.client.UserServiceClient;
import msa.logistics.service.auth.dto.LoginRequestDto;
import msa.logistics.service.auth.dto.SignUpDto;
import msa.logistics.service.auth.security.UserDetailsImpl;
import msa.logistics.service.auth.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response)
            throws IOException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        Collection<GrantedAuthority> roles = ((UserDetailsImpl) authentication.getPrincipal()).getAuthorities();
        List<String> rolesList = roles.stream().map(GrantedAuthority::getAuthority).toList();
        String rolesString = rolesList.toString();

        String jwt = jwtUtil.createToken(username, roles);
        jwtUtil.addJwtToHeader(jwt, response);

        response.addHeader("X-User-Id", username);
        response.addHeader("X-User-Role", rolesString);

        return ResponseEntity.ok().body(username + "login success");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpDto signUpDto) {
        String password = passwordEncoder.encode(signUpDto.getPassword());
        signUpDto.setPassword(password);
        Boolean result = userServiceClient.registerUser(signUpDto);
        if (result) {
            return ResponseEntity.ok().body("signup success");
        } else {
            return ResponseEntity.badRequest().body("signup failed");
        }
    }

}
