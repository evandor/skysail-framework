package io.skysail.server.um.simple.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.um.simple.FileBasedUserManagementProvider;

@Ignore
public class FileBasedUserManagementProviderTest {

    private FileBasedUserManagementProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new FileBasedUserManagementProvider();
        Map<String, String> config = new HashMap<>();
        config.put("users", "admin");
        config.put("admin.id", "#1");
        provider.activate(config);
    }

    @Test
    public void authenticationService_is_available_for_activated_UserManagmentProvider() {
        AuthenticationService service = provider.getAuthenticationService();
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void authorizationService_is_available_for_activated_UserManagmentProvider() {
        //AuthorizationService service = provider.getAuthorizationService();
        //assertThat(service, is(notNullValue()));
    }

    @Test
    public void deactivation_nulls_services() throws Exception {
        provider.deactivate();
        assertThat(provider.getAuthenticationService(), is(nullValue()));
        assertThat(provider.getAuthorizationService(), is(nullValue()));
    }
}
