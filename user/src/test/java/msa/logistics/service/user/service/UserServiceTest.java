package msa.logistics.service.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void isMaster() {
        String yesMaster = "MASTER";
        String yesMasters = "HUB_ADMIN,MASTER";
        String noMaster = "HUB_ADMIN";

        Assertions.assertThat(userService.isMaster(yesMaster)).isTrue();
        Assertions.assertThat(userService.isMaster(yesMasters)).isTrue();
        Assertions.assertThat(userService.isMaster(noMaster)).isFalse();

    }
}