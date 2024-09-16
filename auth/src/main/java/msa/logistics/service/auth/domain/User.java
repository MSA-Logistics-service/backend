package msa.logistics.service.auth.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msa.logistics.service.auth.dto.UserDto;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    private String username;
    private String password;
    private List<String> roles;

    public static User convertToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(new ArrayList<>(userDto.getRoles()))
                .build();
    }
}
