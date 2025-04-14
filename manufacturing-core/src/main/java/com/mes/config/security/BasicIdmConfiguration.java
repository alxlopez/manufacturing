package com.mes.config.security;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * if @Configuration enabled, it is used for testing without basic authentication
 * rather than oaut Authentication
 */
@Configuration
public class BasicIdmConfiguration {
	@Bean
    InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {
        return new InitializingBean() {
            @Override
            public void afterPropertiesSet() throws Exception {
                Group group = identityService.newGroup("user");
                User admin = identityService.newUser("admin");
                group.setName("users");
                group.setType("security-role");
                identityService.deleteGroup("user");
                identityService.saveGroup(group);
                admin.setFirstName("admin");
                admin.setLastName("admin");
                admin.setPassword("admin");
                identityService.deleteUser("admin");
                identityService.saveUser(admin);
                identityService.createMembership("admin", "user");
            }
        };
    }
}
