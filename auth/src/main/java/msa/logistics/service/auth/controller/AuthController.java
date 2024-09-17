package msa.logistics.service.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import msa.logistics.service.auth.dto.LoginRequestDto;
import msa.logistics.service.auth.dto.LoginResponseDto;
import msa.logistics.service.auth.security.UserDetailsImpl;
import msa.logistics.service.auth.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    @PostMapping("/sign-in")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response)
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

        LoginResponseDto loginResponseDto = new LoginResponseDto(username, rolesString);
        String loginJsonResponse = new ObjectMapper().writeValueAsString(loginResponseDto);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(loginJsonResponse);
    }
}