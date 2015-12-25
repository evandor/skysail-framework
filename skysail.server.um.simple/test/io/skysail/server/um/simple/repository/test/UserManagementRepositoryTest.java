package io.skysail.server.um.simple.repository.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.*;

import io.skysail.server.um.simple.repository.UserManagementRepository;

public class UserManagementRepositoryTest {

    private HashMap<String, String> config;

    @Before
    public void setUp() {
        config = new HashMap<String, String>();
    }

    @Test
    public void empty_config_yields_empty_usernamesAndPasswords() {
        UserManagementRepository repository = new UserManagementRepository(new HashMap<String, String>());
        assertThat(repository.getByPrincipal("username"), is(nullValue()));
    }

    @Test
    public void username_with_password_is_found_inUsernamesAndPasswords() {
        config.put("users", "admin");
        config.put("admin.password", "pwd");
        config.put("admin.id", "#1");
        UserManagementRepository repository = new UserManagementRepository(config);
        assertThat(repository.getByPrincipal("#1").getUsername(), is(equalTo("admin")));
        assertThat(repository.getByPrincipal("#1").getPassword(), is(equalTo("pwd")));
    }

    @Test
    public void username_with_role_is_found_inUsernamesAndRoles() {
        config.put("users", "admin");
        config.put("admin.password", "pwd");
        config.put("admin.id", "#1");
        config.put("admin.roles", "role1, role2");
        UserManagementRepository repository = new UserManagementRepository(config);
        assertThat(repository.getByPrincipal("#1").getRoles().size(), is(2));
    }
}
