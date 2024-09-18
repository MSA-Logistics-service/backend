package msa.logistics.service.auth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msa.logistics.service.auth.domain.User;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto implements Serializable {
    private String username;
    private String password;
    private List<String> roles;

    public static UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(new ArrayList<>(user.getRoles()))
                .build();
    }
}
