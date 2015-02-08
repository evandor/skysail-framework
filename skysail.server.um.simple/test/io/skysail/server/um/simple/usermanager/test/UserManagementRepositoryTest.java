package io.skysail.server.um.simple.usermanager.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.um.simple.usermanager.UserManagementRepository;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class UserManagementRepositoryTest {

    private HashMap<String, String> config;

    @Before
    public void setUp() {
        config = new HashMap<String, String>();
    }

    @Test
    public void empty_config_yields_empty_usernamesAndPasswords() {
        UserManagementRepository repository = new UserManagementRepository(new HashMap<String, String>());
        assertThat(repository.getUsernamesAndPasswords().size(), is(0));
    }

    @Test
    public void empty_config_yields_empty_usernamesAndRoles() {
        UserManagementRepository repository = new UserManagementRepository(new HashMap<String, String>());
        assertThat(repository.getUsernamesAndRoles().size(), is(0));
    }

    @Test
    public void username_with_password_is_found_inUsernamesAndPasswords() {
        config.put("users", "admin , user");
        config.put("admin.password", "pwd");
        UserManagementRepository repository = new UserManagementRepository(config);
        assertThat(repository.getUsernamesAndPasswords().size(), is(1));
        assertThat(repository.getUsernamesAndPasswords().get("admin"), is("pwd"));
    }

    @Test
    public void username_with_role_is_found_inUsernamesAndRoles() {
        config.put("users", "admin , user");
        config.put("admin.password", "pwd");
        config.put("admin.roles", "role1, role2");
        UserManagementRepository repository = new UserManagementRepository(config);
        assertThat(repository.getUsernamesAndRoles().size(), is(1));
        assertThat(repository.getUsernamesAndRoles().get("admin").size(), is(2));
    }

}
