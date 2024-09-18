package msa.logistics.service.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto implements Serializable {
    @Size(min = 4, max = 10)  // 길이를 4~10자로 제한
    @Pattern(regexp = "^[a-z0-9]+$")  // 알파벳 소문자와 숫자로만 구성
    private String username;

    private String password;

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String nickname;

    @Pattern(regexp = "MASTER|HUB_ADMIN|HUB_DELIVERY_MANAGER|VENDOR_MANAGER")
    private String role;
}