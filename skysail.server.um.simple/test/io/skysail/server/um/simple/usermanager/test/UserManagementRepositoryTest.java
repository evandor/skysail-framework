package io.skysail.server.um.simple.usermanager.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
        assertThat(repository.getByUsername("username"), is(nullValue()));
    }

    @Test
    public void username_with_password_is_found_inUsernamesAndPasswords() {
        config.put("users", "admin , user");
        config.put("admin.password", "pwd");
        config.put("admin.id", "#1");
        config.put("user.id", "#2");
        UserManagementRepository repository = new UserManagementRepository(config);
        assertThat(repository.getByUsername("admin").getUsername(), is(equalTo("admin")));
        assertThat(repository.getByUsername("admin").getPassword(), is(equalTo("pwd")));
    }

    @Test
    public void username_with_role_is_found_inUsernamesAndRoles() {
        config.put("users", "admin , user");
        config.put("admin.password", "pwd");
        config.put("admin.id", "#1");
        config.put("user.id", "#2");
        config.put("admin.roles", "role1, role2");
        UserManagementRepository repository = new UserManagementRepository(config);
        assertThat(repository.getByUsername("admin").getRoles().size(), is(2));
    }
}
