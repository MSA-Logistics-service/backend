package msa.logistics.service.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.user.common.domain.BaseEntity;
import msa.logistics.service.user.dto.SignUpDto;

@Slf4j
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_users")
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long id;

    @Size(min = 4, max = 10)  // 길이를 4~10자로 제한
    @Pattern(regexp = "^[a-z0-9]+$")  // 알파벳 소문자와 숫자로만 구성
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public static User convertSignUpDtoToUser(SignUpDto signUpDto) {
        return User.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .role(UserRole.fromString(signUpDto.getRole()))
                .build();
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void softDelete(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        this.isDelete = true;
    }
}
