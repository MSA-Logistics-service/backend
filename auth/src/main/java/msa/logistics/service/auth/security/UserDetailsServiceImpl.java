package msa.logistics.service.auth.security;


import msa.logistics.service.auth.client.UserServiceClient;
import msa.logistics.service.auth.domain.User;
import msa.logistics.service.auth.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    public UserDetailsServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto userDto = userServiceClient.getUserByUsername(username);
        User user = User.convertToUser(userDto);
        return new UserDetailsImpl(user);
    }
}