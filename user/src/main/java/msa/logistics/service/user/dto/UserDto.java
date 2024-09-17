package msa.logistics.service.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msa.logistics.service.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
public class UserDto implements Serializable {
    private String username;
    private String password;
    private List<String> roles;

    public static UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().getAuthority());
        userDto.setRoles(roles);
        return userDto;
    }
}
