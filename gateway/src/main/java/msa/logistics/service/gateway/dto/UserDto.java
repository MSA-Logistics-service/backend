package msa.logistics.service.gateway.dto;

import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String userName;
    private Collection<String> roles;
}
